package com.starcom.ui.render;

import java.util.HashMap;

import com.starcom.ui.components.Component;

public class RenderSystem
{
    private static HashMap<Class<? extends Component>,IRenderer> renderers = new HashMap<>();

    public static HashMap<Class<? extends Component>,IRenderer> get() { return renderers; }
}
