package com.generate;

import com.blade.Blade;

/**
 * 代码生成
 * @author YI
 * @date 2018-11-15 11:06:18
 */
public class Application {
    public static void main(String[] args) {
        Blade.of().start(Application.class, args);
    }
}
