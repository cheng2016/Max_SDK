# 项目描述

hy_international_demo 为demo主工程，主要用于测试，或者作为提供给cp那边进行接入的demo

hylibrary 为sdk工程，所有sdk代码都在此，主要控制类为HYSDK、U9Platform，

           其中HYPlatform为辉耀互娱官方渠道类，修改sdk流程一般不许要用到，只要修改辉耀互娱官方渠道才可能用到

## Test_Channel属于cp接入的测试渠道

### AndroidManifest.xml 配置为：

    <meta-data
        android:name="HY_CHANNEL_TYPE"
        android:value="Test" />

### build.gradle 无配置

## HY_Channel属于辉耀互娱官方渠道

### AndroidManifest.xml 配置为：

    <meta-data
        android:name="HY_CHANNEL_TYPE"
        android:value="HY" />

    <!--上架goole play 需要的东西-->
    <!--    <meta-data
            android:name="com.google.android.gms.games.APP_ID"
            android:value="@string/google_app_id" />
        <meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />-->

     <!--AppFlayer  devKey -->
     <meta-data
         android:name="AF_DEV_KEY"
         android:value="WSCXwCE4xFQL9ekVT7rpr7" />

    <activity
        android:name="com.huyu.googlepay.HY_GameCenterActivity"
        android:configChanges="orientation|keyboardHidden|screenSize"
        android:launchMode="singleTop" />

### build.gradle 配置为：

    implementation 'com.appsflyer:af-android-sdk:4+@aar'
    //paypal支付
    implementation 'com.braintreepayments.api:braintree:2.+'

    implementation 'com.google.android.gms:play-services-wallet:16.0.1'
    implementation 'com.android.installreferrer:installreferrer:1.0'

### 注意

其中 google支付需要配置id，具体在string.xml中的 google_app_id 字段 ，如果不生效请查看 HY_GameCenterActivity、HY_Channel类

android 10 以上 AndroidManifest.xml 中 Application 标签必须配置 android:networkSecurityConfig="@xml/network_security_config"  ，否则可能无法访问网络