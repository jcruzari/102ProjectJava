import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.abs;

public abstract class MobileAnimatedActor
   extends AnimatedActor
{
  private static List<Point> path;

   public MobileAnimatedActor(String name, Point position, int rate,
      int animation_rate, List<PImage> imgs)
   {
      super(name, position, rate, animation_rate, imgs);
      path = new LinkedList<>();
   }

   protected Point nextPosition(WorldModel world, Point dest_pt)
   {
      int horiz = Integer.signum(dest_pt.x - getPosition().x);
      Point new_pt = new Point(getPosition().x + horiz, getPosition().y);

      if (horiz == 0 || !canPassThrough(world, new_pt)|| dfs(world, new_pt, dest_pt, path))
         {
         int vert = Integer.signum(dest_pt.y - getPosition().y);
         new_pt = new Point(getPosition().x, getPosition().y + vert);

         if (vert == 0 || !canPassThrough(world, new_pt) || dfs(world, new_pt, dest_pt, path))
         {
            new_pt = getPosition();
         }
      }
      return new_pt;
   }

   protected static boolean adjacent(Point p1, Point p2)
   {
      return (p1.x == p2.x && abs(p1.y - p2.y) == 1) ||
         (p1.y == p2.y && abs(p1.x - p2.x) == 1);
   }

   protected abstract boolean canPassThrough(WorldModel world, Point new_pt);

   public static boolean dfs(WorldModel world, Point pt, Point goal, List<Point> path){
      world.setCell(world.visited, pt, true);
      if(!(world.withinBounds(pt))){
         return false;
      }
      if(world.isOccupied(pt)){
         return false;
      }
      if(world.getCell(world.visited, pt)){
         return	false;
      }
      if(pt.x == goal.x && pt.y == goal.y){
         path.add(0, pt);
         return true;
      }



      boolean found = dfs(world, new Point(pt.x+1, pt.y), goal, path) ||
              dfs(world, new Point(pt.x, pt.y+1), goal, path) ||
              dfs(world, new Point(pt.x-1, pt.y), goal, path) ||
              dfs(world, new Point(pt.x, pt.y-1), goal, path);

      if(found){
         path.add(0, pt);
      }
      return found;
   }
}
