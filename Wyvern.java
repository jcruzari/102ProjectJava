import processing.core.PImage;
import java.util.List;
public class Wyvern extends MobileAnimatedActor{
   public Wyvern(String name, Point position, int rate, int animation_rate, List<PImage> imgs){
      super(name, position, rate, animation_rate, imgs);
   }

   public Action createAction(WorldModel world, ImageStore imageStore){
      Action[] action = {null};
      action[0] = ticks ->{
         //removePendingAction(action[0]);

         WorldEntity target = world.findNearest(getPosition(), Miner.class);
         long nextTime = ticks + getRate();

         if(target != null) {
            Point tPt = target.getPosition();

            if(move(world, target)){
               Quake quake = OreBlob.createQuake(world, tPt, ticks, imageStore);
               Actor burn = new MinerBurning("fire", tPt, 100, 800, 0, 0, Obstacle.class, imageStore.get("fire"));
               world.addEntity(burn);
               burn.schedule(world, 0, imageStore);
               world.addEntity(quake);
            }
         }
         scheduleAction(world, this, createAction(world, imageStore), nextTime);
      };
      return action[0];
   }

   protected boolean canPassThrough(WorldModel world, Point pt){
      return !world.isOccupied(pt) || world.getTileOccupant(pt) instanceof Miner;
   }

   private boolean move(WorldModel world, WorldEntity target)
   {
      if (target == null)
      {
          return false;
      }
      if (adjacent(getPosition(), target.getPosition()))
      {
          target.remove(world);
          return true;
      }
      else
      {
          Point new_pt = nextPosition(world, target.getPosition());
          WorldEntity old_entity = world.getTileOccupant(new_pt);
          if (old_entity != null && old_entity != this)
          {
              old_entity.remove(world);
          }
          world.moveEntity(this, new_pt);
          return false;
      }
   }
}
