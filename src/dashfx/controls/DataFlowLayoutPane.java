/*
 * Copyright (C) 2013 patrick
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dashfx.controls;

import dashfx.lib.controls.Designable;
import dashfx.controls.bases.*;
import dashfx.lib.controls.*;
import dashfx.lib.data.*;
import java.util.ArrayList;
import java.util.EnumSet;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

/**
 *
 * @author patrick
 */
@Designable(value = "Flow", image="/dashfx/controls/res/flow.png",  description = "Automatic flowing pane")
@DesignableProperty(value = {"hgap", "vgap"}, descriptions = {"Horizontal Spacing", "Vertical spacing"})
@DashFXProperties("Sealed: false, Save Children: true")
public class DataFlowLayoutPane extends PaneControlBase<FlowPane>
{
	public DataFlowLayoutPane()
	{
		super(new FlowPane());
        
	}
    
    
	@Designable(value = "Orentation", description = "which direction to flow first.")
	public ObjectProperty<Orientation> orientationProperty()
	{
		return ((FlowPane)getUi()).orientationProperty();
	}

	public Orientation getOrientation()
	{
		return orientationProperty().get();
	}

	public void setUrl(Orientation value)
	{
		orientationProperty().set(value);
	}

	@Override
	public EnumSet<ResizeDirections> getSupportedOps()
	{
		return EnumSet.of(ResizeDirections.LeftRight, ResizeDirections.UpDown, ResizeDirections.NorthEastSouthWest, ResizeDirections.SouthEastNorthWest, ResizeDirections.Move);
	}

	@Override
	public boolean isJumps()
	{
		return true;
	}
    
    private Node[] overlays;
	private Region[] childs;
	private int[] indexes;
	private double[] lastSizeX;
	private double sizeX, sizeY,posX, posY,x,y,  dxDiff;
    

	@Override
	public void BeginDragging(Node[] overlays, Region[] childs, double x, double y, double sizeX, double sizeY, double posX, double posY)
	{      
        this.overlays = overlays;
        this.x = x - getUi().localToScene(getUi().getBoundsInLocal()).getMinX();
        this.y = y - getUi().localToScene(getUi().getBoundsInLocal()).getMinY();
        
	}

	@Override
	public void ContinueDragging(double dx, double dy)           
	{

        for(Node o:overlays){
            
            System.out.println("pos "+(x+dx)+" ,"+(y+dy));
            ArrayList<Node> currentCol = new ArrayList<Node>();
            int colStart = -1;
            double lastY = 0;
            for(Node child:getChildren()){
                if(child.getBoundsInParent().getMinX() <= x+dx && child.getBoundsInParent().getMaxX() >= x+dx){
                    if(child.getBoundsInParent().getMinY() < lastY){
                        break;
                    } else {
                       if(colStart == -1)colStart = getChildren().indexOf(child);
                        currentCol.add(child);                        
                        lastY = child.getBoundsInParent().getMinY();
                    }
                    
                }
            }
            System.out.println("");
            System.out.println(currentCol);
            if(currentCol.size() > 0){
                boolean added = false;
                for(int i = 0; i < currentCol.size(); i++){
                    Node child = currentCol.get(i);
                    if(child.getBoundsInParent().getMinY() + child.getBoundsInParent().getHeight()/2 > y+dy){
                        if(child != o){
                            getChildren().remove(o);
                            System.out.println(getChildren().size()+"    "+getChildren().indexOf(child));
                            getChildren().add(getChildren().indexOf(child), o);
                        }
                        
                        added = true;

                        break;
                    }
                }
                if(!added){
                    getChildren().remove(o);
                    currentCol.remove(o);
                    getChildren().add(colStart+currentCol.size(),o);
                }
            }
        }
		
	}

	@Override
	public void FinishDragging()
	{
		overlays = childs = null;
        sizeX = sizeY = posX = posY = 0;
	}

	@Override
	public void addChildAt(Node child, double x, double y)
	{
		ui.getChildren().add(child);
		//TODO: Fix
	}

	@Override
	public void editNested(Node overlay, Runnable onExitRequest)
	{
		// TODO: FIXME
	}

	@Override
	public void exitNested()
	{
		// TODO: FIXME
	}

	@Override
	public void zEdit(Node child, ZPositions diff)
	{
		// dnc
	}

	@Override
	public boolean isAppendable()
	{
		return true;
	}
}
