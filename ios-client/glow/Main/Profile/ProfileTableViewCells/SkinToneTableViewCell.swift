//
//  SkinToneTableViewCell.swift
//  glow
//
//  Created by Dreams on 26/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import VerticalSteppedSlider

protocol changeSkinToneColorDelegate: class {
   func didChangeSkinColor(_ hexString: String)
}

class SkinToneTableViewCell: UITableViewCell, ChromaColorPickerDelegate {

   // @IBOutlet weak var colorPicker: ChromaColorPicker!
    @IBOutlet weak var sliderView: UIView!
    var skinTone: String = ""
    var delegate: changeSkinToneColorDelegate?
    let colorPicker = ChromaColorPicker()
    let brightnessSlider = ChromaBrightnessSlider()
    private var homeHandle: ChromaColorHandle!
    private let defaultColorPickerSize = CGSize(width: 320, height: 320)
    private let brightnessSliderWidthHeightRatio: CGFloat = 0.1
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        setupColorPicker()
        setupBrightnessSlider()
        setupColorPickerHandles()
    }

    private func setupColorPicker() {
        colorPicker.isHidden = true
        colorPicker.delegate = self
        colorPicker.translatesAutoresizingMaskIntoConstraints = false
        sliderView.addSubview(colorPicker)
        
        _ = -defaultColorPickerSize.height / 6
        
        NSLayoutConstraint.activate([
            colorPicker.centerXAnchor.constraint(equalTo: sliderView.centerXAnchor),
            colorPicker.centerYAnchor.constraint(equalTo: sliderView.centerYAnchor, constant: 0),
            colorPicker.widthAnchor.constraint(equalToConstant: defaultColorPickerSize.width),
            colorPicker.heightAnchor.constraint(equalToConstant: defaultColorPickerSize.height)
        ])
    }
    
    private func setupBrightnessSlider() {
        brightnessSlider.connect(to: colorPicker)
        
        // Style
        brightnessSlider.trackColor = UIColor.blue
        brightnessSlider.handle.borderWidth = 3.0 // Example of customizing the handle's properties.
        brightnessSlider.handle.handleColor = UIColor.hexStringToUIColor(hex: skinTone)
        // Layout
        brightnessSlider.translatesAutoresizingMaskIntoConstraints = false
        sliderView.addSubview(brightnessSlider)
        brightnessSlider.addTarget(self, action: #selector(changeValue(_:)), for: .touchUpInside)
        NSLayoutConstraint.activate([
            brightnessSlider.centerXAnchor.constraint(equalTo: sliderView.centerXAnchor),
            brightnessSlider.topAnchor.constraint(equalTo: sliderView.bottomAnchor, constant: -50),
            brightnessSlider.widthAnchor.constraint(equalTo: sliderView.widthAnchor, multiplier: 0.9),
            brightnessSlider.heightAnchor.constraint(equalTo: brightnessSlider.widthAnchor, multiplier: brightnessSliderWidthHeightRatio)
        ])
    }
    @objc func changeValue(_ sender: Any) {
        print("\(brightnessSlider.currentValue)")
        print("\(brightnessSlider.currentColor)")
        let colorTone = "#" + brightnessSlider.currentColor.hexCode
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: "SkinTone"), object: colorTone)
       // delegate?.didChangeSkinColor(colorTone)
    }
    private func setupColorPickerHandles() {
        // (Optional) Assign a custom handle size - all handles appear as the same size
        // colorPicker.handleSize = CGSize(width: 48, height: 60)
        
        // 1. Add handle and then customize
        addHomeHandle()
        
        // 2. Add a handle via a color
        let peachColor = UIColor(red: 1, green: 203 / 255, blue: 164 / 255, alpha: 1)
        colorPicker.addHandle(at: peachColor)
        
        // 3. Create a custom handle and add to picker
        let customHandle = ChromaColorHandle()
        customHandle.color = UIColor(red: 1, green: 203 / 255, blue: 164 / 255, alpha: 1)
        colorPicker.addHandle(customHandle)
    }
    
    private func addHomeHandle() {
        homeHandle = colorPicker.addHandle(at: .blue)
        
        // Setup custom handle view with insets
        let customImageView = UIImageView(image: #imageLiteral(resourceName: "ic_icon").withRenderingMode(.alwaysTemplate))
        customImageView.contentMode = .scaleAspectFit
        customImageView.tintColor = .white
      //  homeHandle.accessoryView = customImageView
        
       // homeHandle.accessoryViewEdgeInsets = UIEdgeInsets(top: 2, left: 4, bottom: 4, right: 4)
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
extension SkinToneTableViewCell {
    
    func colorPickerHandleDidChange(_ colorPicker: ChromaColorPicker, handle: ChromaColorHandle, to color: UIColor) {
        // Here I can detect when the color is too bright to show a white icon
        // on the handle and change its tintColor.
        if handle === homeHandle {

            UIView.animate(withDuration: 0.2, animations: {
               // imageView.tintColor = colorIsBright ? .black : .white
            }, completion: nil)
        }
    }
}
public extension UIColor{
    @objc var hexCode: String {
        get{
            let colorComponents = self.cgColor.components!
            if colorComponents.count < 4 {
                return String(format: "%02x%02x%02x", Int(colorComponents[0]*255.0), Int(colorComponents[0]*255.0),Int(colorComponents[0]*255.0)).uppercased()
            }
            return String(format: "%02x%02x%02x", Int(colorComponents[0]*255.0), Int(colorComponents[1]*255.0),Int(colorComponents[2]*255.0)).uppercased()
        }
    }
    
    //Amount should be between 0 and 1
    @objc func lighterColor(_ amount: CGFloat) -> UIColor{
        return UIColor.blendColors(color: self, destinationColor: UIColor.white, amount: amount)
    }
    
    @objc func darkerColor(_ amount: CGFloat) -> UIColor{
        return UIColor.blendColors(color: self, destinationColor: UIColor.black, amount: amount)
    }
    
    static func blendColors(color: UIColor, destinationColor: UIColor, amount : CGFloat) -> UIColor{
        var amountToBlend = amount;
        if amountToBlend > 1{
            amountToBlend = 1.0
        }
        else if amountToBlend < 0{
            amountToBlend = 0
        }
        
        var r,g,b, alpha : CGFloat
        r = 0
        g = 0
        b = 0
        alpha = 0
        color.getRed(&r, green: &g, blue: &b, alpha: &alpha) //gets the rgba values (0-1)
        
        //Get the destination rgba values
        var dest_r, dest_g, dest_b, dest_alpha : CGFloat
        dest_r = 0
        dest_g = 0
        dest_b = 0
        dest_alpha = 0
        destinationColor.getRed(&dest_r, green: &dest_g, blue: &dest_b, alpha: &dest_alpha)
        
        r = amountToBlend * (dest_r * 255) + (1 - amountToBlend) * (r * 255)
        g = amountToBlend * (dest_g * 255) + (1 - amountToBlend) * (g * 255)
        b = amountToBlend * (dest_b * 255) + (1 - amountToBlend) * (b * 255)
        alpha = abs(alpha / dest_alpha)
        
        return UIColor(red: r/255.0, green: g/255.0, blue: b/255.0, alpha: alpha)
    }
    
}
