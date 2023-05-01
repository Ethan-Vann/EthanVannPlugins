package com.example.pathmarker;

import net.runelite.api.Client;

import javax.inject.Inject;
import java.awt.event.MouseEvent;

public class MouseListener implements net.runelite.client.input.MouseListener
{
    @Inject
    Client client;

    @Inject
    PathMarkerPlugin plugin;

    @Override
    public MouseEvent mouseClicked(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mousePressed(MouseEvent mouseEvent)
    {
        if (mouseEvent.getButton()==MouseEvent.BUTTON1)
        {
            plugin.setLeftClicked(true);
            plugin.setLastMouseCanvasPosition(client.getMouseCanvasPosition());
        }
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseReleased(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseEntered(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseExited(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseDragged(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseMoved(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }
}
