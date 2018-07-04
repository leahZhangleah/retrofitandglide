package com.example.android.retrofitandglide.dagger;

import com.example.android.retrofitandglide.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MovieModule.class})
public interface MovieComponent {
    void inject(MainActivity activity);
}
