package com.example.aplikacjaism;

import android.app.Application;
import android.content.res.Resources;
import android.content.res.TypedArray;

public class MyApp extends Application {
    public static String[] pizzaName;
    public static String[] pizzaDescription;
    public static TypedArray pizzaImage;
    public static int size;

    @Override
    public void onCreate() {
        super.onCreate();

        Resources resources = getResources();
        pizzaName = resources.getStringArray(R.array.pizzaName);
        pizzaImage = resources.obtainTypedArray(R.array.pizzaImage);
        pizzaDescription = resources.getStringArray(R.array.pizzaDescription);

        int size0 = pizzaName.length;
        int size1 = pizzaImage.length();
        int size2 = pizzaDescription.length;

        if (size0 == size1 && size1 == size2) size = size0;
        else size = 0;
    }
}
