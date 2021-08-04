package com.glowteam.Interfaces;

public class Interface {
    public static OnProductAdded mOnProductAdded;
    public static OnProductAdded1 mOnProductAdded1;
    public static OnPermissionGranted mOnPermissionGranted;

    public interface OnProductAdded {
        void OnProductAdded();
    }

    public static void SetOnProductAdded(OnProductAdded onProductAdded) {
        mOnProductAdded = onProductAdded;
    }


    public interface OnProductAdded1 {
        void OnProductAdded1();
    }

    public static void SetOnProductAdded1(OnProductAdded1 onProductAdded) {
        mOnProductAdded1 = onProductAdded;
    }

    public interface OnPermissionGranted {
        void OnPermissionGranted();
    }

    public static void SetOnPermissionGranted(OnPermissionGranted onPermissionGranted) {
        mOnPermissionGranted = onPermissionGranted;
    }

}
