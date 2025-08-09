package com.starcom.ui.render;

import com.starcom.ui.components.Component;
import com.starcom.ui.frame.IFrameGraphics;

public interface IRenderer
{
    /** Renders the specific component.
     * When render was completed, c.setShouldRender(false) should be executed.
     * @param c The component, that should be rendered.
     * @param frameGraphics The graphics where to draw.
     * @param xShift The shift of x coordinate.
     * @param yShift The shift of y coordinate.
     */
    public void render(Component c, IFrameGraphics frameGraphics, int xShift, int yShift);
}
