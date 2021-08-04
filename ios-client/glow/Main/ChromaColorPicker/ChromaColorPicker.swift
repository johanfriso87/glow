//
//  ChromaColorPicker.swift
//
//  Copyright © 2016 Jonathan Cardasis. All rights reserved.
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in all
//  copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//  SOFTWARE.
//

import UIKit
import NotificationCenter

@objc protocol ChromaColorPickerDelegate {
    /* Called when the user taps the add button in the center */
    func colorPickerDidChooseColor(_ colorPicker: ChromaColorPicker, color: UIColor)
}

@objc class ChromaColorPicker: UIControl {
    @objc var hexLabel: UILabel!
    @objc open var shadeSlider: ChromaShadeSlider!
    open var handleView: ChromaHandle!
    open var handleLine: CAShapeLayer!
    open var addButton: ChromaAddButton!
    open var colorToggleButton: ColorModeToggleButton!
    fileprivate var lastSelectedColor: UIColor!
    
    
//    var colorArray: [UIColor] = []
    
    private var modeIsGrayscale: Bool {
        return colorToggleButton.colorState == .grayscale
    }
    private enum ColorSpace {
        case rainbow
        case grayscale
    }
    
    var currentColor: UIColor!
    
    open var supportsShadesOfGray: Bool = false {
        didSet {
            if supportsShadesOfGray {
                colorToggleButton.isHidden = false
            }
            else {
                colorToggleButton.isHidden = true
            }
        }
    }
    @objc var delegate: ChromaColorPickerDelegate?
     var currentAngle: Float = 0
     private(set) var radius: CGFloat = 0
     @objc var stroke: CGFloat = 1
     @objc var padding: CGFloat = 15
    
     var handleSize: CGSize{
        get{ return CGSize(width: self.bounds.width * 0.15, height: self.bounds.height * 0.15) }
    }
    
    //MARK: - Initialization
    override public init(frame: CGRect) {
        super.init(frame: frame)
        self.commonInit()
    }
    
    required public init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.commonInit()
    }
    
    private func commonInit(){
        self.backgroundColor = UIColor.clear
        self.currentColor = UIColor.yellow//self.hexStringToUIColor(hex: "#36BB9B")
 
        let minDimension = min(self.bounds.size.width, self.bounds.size.height)
        radius = minDimension/2 - handleSize.width/2
        
        /* Setup Handle */
        handleView = ChromaHandle(frame: CGRect(x: 0,y: 0, width: handleSize.width, height: handleSize.height))
        handleView.shadowOffset = CGSize(width: 0,height: 2)
        
        /* Setup pan gesture for handle */
        let panRecognizer = UIPanGestureRecognizer(target: self, action: #selector(ChromaColorPicker.handleWasMoved(_:)))
        handleView.addGestureRecognizer(panRecognizer)
        
        /* Setup Add Button */
        addButton = ChromaAddButton()
        self.layoutAddButton() //layout frame
        addButton.addTarget(self, action: #selector(ChromaColorPicker.addButtonPressed(_:)), for: .touchUpInside)
        addButton.isHidden = true
        /* Setup Handle Line */
        handleLine = CAShapeLayer()
        handleLine.lineWidth = 0
        handleLine.strokeColor = UIColor.white.withAlphaComponent(0.2).cgColor
        
        /* Setup Color Hex Label */
        hexLabel = UILabel()
        self.layoutHexLabel() //layout frame
        hexLabel.layer.cornerRadius = 2
        hexLabel.adjustsFontSizeToFitWidth = true
        hexLabel.textAlignment = .center
        hexLabel.textColor = UIColor.clear//UIColor(red: 51/255.0, green:51/255.0, blue: 51/255.0, alpha: 0.65)
        
        /* Setup Shade Slider */
        shadeSlider = ChromaShadeSlider()
        shadeSlider.delegate = self
        self.layoutShadeSlider()
        shadeSlider.addTarget(self, action: #selector(ChromaColorPicker.sliderEditingDidEnd(_:)), for: .editingDidEnd)
        
        /* Setup Color Mode Toggle Button */
        colorToggleButton = ColorModeToggleButton()
        self.layoutColorToggleButton() //layout frame
        colorToggleButton.colorState = .hue // Default as starting state is hue
        colorToggleButton.addTarget(self, action: #selector(togglePickerColorMode), for: .touchUpInside)
        colorToggleButton.isHidden = !supportsShadesOfGray // default to hiding if not supported
        
        /* Add components to view */
        self.layer.addSublayer(handleLine)
        self.addSubview(shadeSlider)
        self.addSubview(hexLabel)
        self.addSubview(handleView)
        self.addSubview(addButton)
        self.addSubview(colorToggleButton)
    }
    
    open override func layoutSubviews() {
        super.layoutSubviews()
        layout()
    }
    func hexStringToUIColor (hex:String) -> UIColor {
        var cString:String = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()
        
        if (cString.hasPrefix("#")) {
            cString.remove(at: cString.startIndex)
        }
        
        if ((cString.count) != 6) {
            return UIColor.gray
        }
        
        var rgbValue:UInt32 = 0
        Scanner(string: cString).scanHexInt32(&rgbValue)
        
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    override open func willMove(toSuperview newSuperview: UIView?) {
        /* Get the starting color */
        currentColor = colorOnWheelFromAngle(currentAngle)
        handleView.center = positionOnWheelFromAngle(currentAngle) //update pos for angle
        self.layoutHandleLine() //layout the lines positioning
        
        handleView.color = currentColor
        addButton.color = currentColor
        shadeSlider.primaryColor = currentColor
        self.updateHexLabel() //update for hex value
    }
    
    @objc func adjustToColor(_ color: UIColor){
        /* Apply saturation and brightness from previous color to current one */
        var saturation: CGFloat = 0.0
        var brightness: CGFloat = 0.0
        var hue: CGFloat = 0.0
        var alpha: CGFloat = 0.0
        
        color.getHue(&hue, saturation: &saturation, brightness: &brightness, alpha: &alpha)
        let newColor = UIColor(hue: hue, saturation: saturation, brightness: brightness, alpha: alpha)
        
        if newColor.hexCode == "000000" {
            currentAngle = angleForColor(newColor)
            currentColor = newColor
             return
        }
//        else {
//              shadeSlider.primaryColor = UIColor(hue: hue, saturation: 1, brightness: 1, alpha: 1)
//        }
        /* Set the slider value for the new color and update addButton */
//        shadeSlider.primaryColor = UIColor(hue: hue, saturation: 1, brightness: 1, alpha: 1) //Set a color recognzied on the color wheel
        
        /* Update the angle and currentColor */
        currentAngle = angleForColor(newColor)
        currentColor = newColor
        if brightness < 1.0 && saturation < 1.0 {
            /* Modifies the Shade Slider to handle adjusting to colors outside of the Chroma scope */
            shadeSlider.primaryColor = UIColor(hue: hue, saturation: saturation, brightness: brightness, alpha: 1)
            shadeSlider.currentValue = 0
        } else
            if brightness < 1.0 { //currentValue is on the left side of the slider
            shadeSlider.currentValue = brightness-1
        }else{
            shadeSlider.currentValue = -(saturation-1)
        }
        shadeSlider.updateHandleLocation() //update the handle location now that the value is set
        addButton.color = newColor
        
        /* Will layout based on new angle */
        self.layoutHandle()
        self.layoutHandleLine()
        self.updateHexLabel()
    }
    
    //MARK: - Handle Touches
    override open func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?){
        //Overriden to prevent uicontrolevents being called from the super
    }
    override open func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?){
        let touchPoint = touches.first!.location(in: self)
        if handleView.frame.contains(touchPoint) {
            self.sendActions(for: .touchDown)
            
            /* Enlarge Animation */
            UIView.animate(withDuration: 0.15, delay: 0, options: .curveEaseIn, animations: { () -> Void in
                self.handleView.transform = CGAffineTransform(scaleX: 1.45, y: 1.45)
                }, completion: nil)
        }
    }
    override open func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        //Run this animation after a pan or here if touches are released
        if handleView.transform.d > 1 { //if scale is larger than 1 (already animated)
            self.executeHandleShrinkAnimation()
        }
    }
    
  @objc func handleWasMoved(_ recognizer: UIPanGestureRecognizer) {
        switch(recognizer.state){

        case UIGestureRecognizer.State.changed:
            let touchPosition = recognizer.location(in: self)
            self.moveHandleTowardPoint(touchPosition)
            self.sendActions(for: .touchDragInside)
            break
        
        case UIGestureRecognizer.State.ended:
            /* Shrink Animation */
            self.executeHandleShrinkAnimation()
            break
            
        default:
            break
        }
    }
    
    private func executeHandleShrinkAnimation(){
        self.sendActions(for: .touchUpInside)
        self.sendActions(for: .editingDidEnd)
        UIView.animate(withDuration: 0.15, delay: 0, options: .curveEaseOut, animations: { () -> Void in
            self.handleView.transform = CGAffineTransform(scaleX: 1, y: 1)
            }, completion: nil)
    }
    
    private func moveHandleTowardPoint(_ point: CGPoint){
        currentAngle = angleToCenterFromPoint(point) //Find the angle of point to the frames center
        
        //Layout Handle
        self.layoutHandle()
        
        //Layout Line
        self.layoutHandleLine()
        
        if modeIsGrayscale {
            // If mode is grayscale do not update colors and end early
            return
        }
        
        //Update color for shade slider
        shadeSlider.primaryColor = handleView.color//currentColor
        
        //Update color for add button if a shade isnt selected
        if shadeSlider.currentValue == 0 {
            self.updateCurrentColor(shadeSlider.currentColor)
        }
        delegate?.colorPickerDidChooseColor(self, color: currentColor)
        //Update Text Field display value
        self.updateHexLabel()
    }
    
  @objc func addButtonPressed(_ sender: ChromaAddButton){
        //Do a 'bob' animation
        UIView.animate(withDuration: 0.2,
                delay: 0,
                options: .curveEaseIn,
                animations: { () -> Void in
                    sender.transform = CGAffineTransform(scaleX: 1.1, y: 1.1)
                }, completion: { (done) -> Void in
                    UIView.animate(withDuration: 0.1, animations: { () -> Void in
                        sender.transform = CGAffineTransform(scaleX: 1, y: 1)
                    })
                })
        
        delegate?.colorPickerDidChooseColor(self, color: sender.color) //Delegate call
    }
    
  @objc func sliderEditingDidEnd(_ sender: ChromaShadeSlider){
        self.sendActions(for: .editingDidEnd)
    }
    
    //MARK: - Drawing
    override open func draw(_ rect: CGRect) {
        super.draw(rect)
        let ctx = UIGraphicsGetCurrentContext()
        let colorSpace: ColorSpace = (modeIsGrayscale) ? .grayscale : .rainbow
        
        drawCircleRing(in: ctx, outerRadius: radius - padding, innerRadius: radius - stroke - padding, resolution: 1, colorSpace: colorSpace)
    }
    
    /*
    Resolution should be between 0.1 and 1
    colorSpace - either rainbow or grayscale
    */
    
 
    private func drawCircleRing(in context: CGContext?, outerRadius: CGFloat, innerRadius: CGFloat, resolution: Float, colorSpace: ColorSpace){
        context?.saveGState()
        context?.translateBy(x: self.bounds.midX, y: self.bounds.midY) //Move context to center
        
        let subdivisions:CGFloat = CGFloat(resolution * 512) //Max subdivisions of 512

        let innerHeight = (CGFloat.pi*innerRadius)/subdivisions //height of the inner wall for each segment
        let outterHeight = (CGFloat.pi*outerRadius)/subdivisions
        
        let segment = UIBezierPath()
        segment.move(to: CGPoint(x: innerRadius, y: -innerHeight/2))
        segment.addLine(to: CGPoint(x: innerRadius, y: innerHeight/2))
        segment.addLine(to: CGPoint(x: outerRadius, y: outterHeight/2))
        segment.addLine(to: CGPoint(x: outerRadius, y: -outterHeight/2))
        segment.close()
        
        
        //Draw each segment and rotate around the center
        for i in 0 ..< Int(ceil(subdivisions)) {
            if modeIsGrayscale {
                UIColor(red: 0.5, green: 0.5, blue: 0.5, alpha: 1).set() //Gray
            }
            else { // Draw rainbow
//                let color = self.colorArray[i].hsba
//                 UIColor(hue: color.h, saturation: color.s, brightness: color.b, alpha: color.a).set()
                if (i > 33 || i < 208) {
                    UIColor(hue: CGFloat(i)/subdivisions, saturation: 1, brightness: 1, alpha: 1).set()
                }
                else {
                    UIColor(hue: CGFloat(32.0)/subdivisions, saturation: 1, brightness: 1, alpha: 1).set()
                }
            }
            
            segment.fill()
            let lineTailSpace = (CGFloat.pi*2)*outerRadius/subdivisions  //The amount of space between the tails of each segment
            segment.lineWidth = lineTailSpace //allows for seemless scaling
            segment.stroke()
            
            //Rotate to correct location
            let rotate = CGAffineTransform(rotationAngle: -((CGFloat.pi*2)/subdivisions)) //rotates each segment
            segment.apply(rotate)
        }
        
        context?.translateBy(x: -self.bounds.midX, y: -self.bounds.midY) //Move context back to original position
        context?.restoreGState()
    }
    
    
    //MARK: - Layout Updates
    /* Re-layout view and all its subview and drawings */
    open func layout() {
        self.setNeedsDisplay() //mark view as dirty
        
        let minDimension = min(self.bounds.size.width, self.bounds.size.height)
        radius = minDimension/2 - handleSize.width/2 //create radius for new size
        
        self.layoutAddButton()
        
        //Update handle's size
        let size = CGSize(width: self.handleSize.height, height: self.handleSize.height)
        handleView.frame = CGRect(origin: .zero, size: size)
//        handleView.frame = CGRect(origin: .zero, size: handleSize)
        self.layoutHandle()
        
        //Ensure colors are updated
        self.updateCurrentColor(handleView.color)
        shadeSlider.primaryColor = handleView.color
        
        self.layoutShadeSlider()
        self.layoutHandleLine()
        self.layoutHexLabel()
        self.layoutColorToggleButton()
    }
    
    open func layoutAddButton(){
        let addButtonSize = CGSize(width: self.bounds.width/5, height: self.bounds.height/5)
        addButton.frame = CGRect(x: self.bounds.midX - addButtonSize.width/2, y: self.bounds.midY - addButtonSize.height/2, width: addButtonSize.width, height: addButtonSize.height)
    }
    
    /*
    Update the handleView's position and color for the currentAngle
    */
    func layoutHandle(){
        let angle = currentAngle //Preserve value in case it changes
        let newPosition = positionOnWheelFromAngle(angle) //find the correct position on the color wheel
        
        //Update handle position
        handleView.center = newPosition
        
        if !modeIsGrayscale {
            //Update color for the movement when color mode is hue
            
            
//            if UserDefaults.standard.value(forKey: "SOURCE_COLOR") != nil {
//                let  hexSourceColor = "#\(UserDefaults.standard.value(forKey: "SOURCE_COLOR")!)"
//                var color = hexStringToUIColor(hex: hexSourceColor)
//                handleView.color = color
//            } else {
//                if UIDevice.current.userInterfaceIdiom == UIUserInterfaceIdiom.pad {
//                    var color = hexStringToUIColor(hex: "23AEA3")
//                    handleView.color = color
//                } else {
//                    var color = hexStringToUIColor(hex: "36BB9B")
//                    handleView.color = color
//                }
//            }
            
            handleView.color = colorOnWheelFromAngle(angle)
        }
    }
    
    
    /*
    Updates the line view's position for the current angle
    Pre: dependant on addButtons position & current angle
    */
    func layoutHandleLine(){
        let linePath = UIBezierPath()
        linePath.move(to: addButton.center)
        linePath.addLine(to: positionOnWheelFromAngle(currentAngle))
        handleLine.path = linePath.cgPath
    }
    
    /*
    Pre: dependant on addButtons position
    */
    func layoutHexLabel(){
        hexLabel.frame = CGRect(x: 0, y: 0, width: addButton.bounds.width*1.5, height: addButton.bounds.height/3)
        let y = (addButton.frame.origin.y + (padding + handleView.frame.height/2 + stroke/2))
        hexLabel.center = CGPoint(x: self.bounds.midX, y:y/1.75)
       //Divided by 1.75 not 2 to make it a bit lower
        hexLabel.font = UIFont(name: "Menlo-Regular", size: hexLabel.bounds.height)
    }
    
    /*
    Pre: dependant on radius
    */
    func layoutShadeSlider(){
        /* Calculate proper length for slider */
        let centerPoint = CGPoint(x: bounds.midX, y: bounds.midY)
        let insideRadius = radius - padding
        
        let pointLeft = CGPoint(x: centerPoint.x + insideRadius*CGFloat(cos(7*Double.pi/6)), y: centerPoint.y - insideRadius*CGFloat(sin(7*Double.pi/6)))
        let pointRight = CGPoint(x: centerPoint.x + insideRadius*CGFloat(cos(11*Double.pi/6)), y: centerPoint.y - insideRadius*CGFloat(sin(11*Double.pi/6)))
        let deltaX = pointRight.x - pointLeft.x //distance on circle between points at 7pi/6 and 11pi/6
        

        let sliderSize = CGSize(width: (deltaX * 0.75), height: (0.08 * (bounds.height - padding*2))+10)//bounds.height
        
        shadeSlider.frame = CGRect(x: bounds.midX - sliderSize.width/2, y: self.bounds.midY - sliderSize.height/2, width: sliderSize.width, height: sliderSize.height)
                                 
//        shadeSlider.frame = CGRect(x: bounds.midX - sliderSize.width/2, y: pointLeft.y - sliderSize.height/2, width: sliderSize.width, height: sliderSize.height)
        shadeSlider.handleCenterX = shadeSlider.bounds.width/2
        shadeSlider.layoutLayerFrames() //call sliders' layout function
    }
    
    /*
     Pre: dependant on addButton
    */
    func layoutColorToggleButton() {
        let inset = bounds.height/16
        colorToggleButton.frame = CGRect(x: inset, y: inset, width: addButton.frame.width/2.5, height: addButton.frame.width/2.5)
        colorToggleButton.layoutSubviews()
    }
    
    func updateHexLabel(){
        hexLabel.text = "#" + currentColor.hexCode
    }
    
    func updateCurrentColor(_ color: UIColor){
        currentColor = color
        addButton.color = color
        self.sendActions(for: .valueChanged)
    }
    
  @objc open func togglePickerColorMode() {
        colorToggleButton.isEnabled = false // Lock
        
        // Redraw Assets (i.e. Large circle ring)
        setNeedsDisplay()
        
        // Update subcomponents for color change
        if modeIsGrayscale {
            //Change color of colorToggleButton to the last handle color
            let lightColor = handleView.color
            let shadedColor = handleView.color.darkerColor(0.25)
            colorToggleButton.hueColorGradientLayer.colors = [lightColor.cgColor, shadedColor.cgColor]
            
            let gray = UIColor(red: 0.5, green: 0.5, blue: 0.5, alpha: 1)
            self.handleView.color = gray
            self.updateCurrentColor(gray)
            self.updateHexLabel()
            
            //Update color for shade slider
            shadeSlider.primaryColor = gray
        }
        else {
            // Update for normal rainbow
            
            // Use color stored in toggle button (set above), or else default to the angle it is at
            var hueColor: UIColor
            if let storedColor = colorToggleButton.hueColorGradientLayer.colors?[0] {
                hueColor = UIColor(cgColor: (storedColor as! CGColor))
                
                currentAngle = angleForColor(hueColor)
                self.layoutHandleLine()
                self.layoutHandle()
            }
            else {
                let currentAngle = self.angleToCenterFromPoint(self.handleView.center)
                hueColor = self.colorOnWheelFromAngle(currentAngle)
            }
            
            self.handleView.color = hueColor
            self.updateCurrentColor(hueColor)
            self.updateHexLabel()
            
            //Update color for shade slider
            shadeSlider.primaryColor = hueColor
        }
        
        colorToggleButton.isEnabled = true // Unlock
    }
    
    
    //MARK: - Helper Methods
    private func angleToCenterFromPoint(_ point: CGPoint) -> Float {
        let deltaX = Float(self.bounds.midX - point.x)
        let deltaY = Float(self.bounds.midY - point.y)
        let angle = atan2f(deltaX, deltaY)
        
        // Convert the angle to be between 0 and 2PI
        var adjustedAngle = angle + Float.pi/2
        if (adjustedAngle < 0){ //Left side (Q2 and Q3)
            adjustedAngle += Float.pi*2
        }

        return adjustedAngle
    }
    
    /* Find the angle relative to the center of the frame and uses the angle to find what color lies there */
    private func colorOnWheelFromAngle(_ angle: Float) -> UIColor {
        return UIColor(hue: CGFloat(Double(angle)/(2*Double.pi)), saturation: 1, brightness: 1, alpha: 1)
    }
    
    private func angleForColor(_ color: UIColor) -> Float {
        var hue: CGFloat = 0
        color.getHue(&hue, saturation: nil, brightness: nil, alpha: nil)
        return Float(hue * CGFloat.pi * 2)
    }
    
    /* Returns a position centered on the wheel for a given angle */
    private func positionOnWheelFromAngle(_ angle: Float) -> CGPoint{
        let buffer = padding + stroke/2
        return CGPoint(x: self.bounds.midX + ((radius - buffer) * CGFloat(cos(-angle))), y: self.bounds.midY + ((radius - buffer) * CGFloat(sin(-angle))))
    }
}


extension ChromaColorPicker: ChromaShadeSliderDelegate{
    public func shadeSliderChoseColor(_ slider: ChromaShadeSlider, color: UIColor) {
        self.lastSelectedColor = color
        delegate?.colorPickerDidChooseColor(self, color: color)
        self.updateCurrentColor(color) //update main controller for selected color
        self.updateHexLabel()
    }
}
extension UIColor {
    var hsba:(h: CGFloat, s: CGFloat,b: CGFloat,a: CGFloat) {
        var h: CGFloat = 0, s: CGFloat = 0, b: CGFloat = 0, a: CGFloat = 0
        self.getHue(&h, saturation: &s, brightness: &b, alpha: &a)
        return (h: h, s: s, b: b, a: a)
    }
}
