//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Nylocas;

import net.runelite.client.ui.overlay.Overlay;

import java.awt.*;

public class NyloSelectionManager extends Overlay {
    private final NyloSelectionBox melee;
    private final NyloSelectionBox mage;
    private final NyloSelectionBox range;
    private boolean isHidden = true;
    private Rectangle meleeBounds = new Rectangle();
    private Rectangle rangeBounds = new Rectangle();
    private Rectangle mageBounds = new Rectangle();

    public NyloSelectionManager(NyloSelectionBox melee, NyloSelectionBox mage, NyloSelectionBox range) {
        this.mage = mage;
        this.melee = melee;
        this.range = range;
    }

    public Dimension render(Graphics2D graphics) {
        if (this.isHidden) {
            return null;
        } else {
            Dimension meleeD = this.melee.render(graphics);
            graphics.translate(meleeD.width + 1, 0);
            Dimension rangeD = this.range.render(graphics);
            graphics.translate(rangeD.width + 1, 0);
            Dimension mageD = this.mage.render(graphics);
            graphics.translate(-meleeD.width - rangeD.width - 2, 0);
            this.meleeBounds = new Rectangle(this.getBounds().getLocation(), meleeD);
            this.rangeBounds = new Rectangle(new Point(this.getBounds().getLocation().x + meleeD.width + 1, this.getBounds().y), rangeD);
            this.mageBounds = new Rectangle(new Point(this.getBounds().getLocation().x + meleeD.width + 1 + rangeD.width + 1, this.getBounds().y), mageD);
            return new Dimension(meleeD.width + rangeD.width + mageD.width, Math.max(Math.max(meleeD.height, rangeD.height), mageD.height));
        }
    }

    public NyloSelectionBox getMelee() {
        return this.melee;
    }

    public NyloSelectionBox getMage() {
        return this.mage;
    }

    public NyloSelectionBox getRange() {
        return this.range;
    }

    public boolean isHidden() {
        return this.isHidden;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public Rectangle getMeleeBounds() {
        return this.meleeBounds;
    }

    public Rectangle getRangeBounds() {
        return this.rangeBounds;
    }

    public Rectangle getMageBounds() {
        return this.mageBounds;
    }
}
