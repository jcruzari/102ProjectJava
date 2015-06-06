import processing.core.PImage;
import java.util.List;
public  class MinerBurning extends Miner{

   public MinerBurning(String name, Point position, int rate, int animationRate,
                       int resource_limit, int resource_count, Class<?> seeking,
                       List<PImage> imgs){
      super(name, position, rate, animationRate, resource_limit, resource_count,
              Obstacle.class,imgs);
   }

   public Action createAction(WorldModel world, ImageStore imageStore){
      Action[] action = { null };
      action[0] = ticks -> {
         removePendingAction(action[0]);

         WorldEntity target = world.findNearest(getPosition(), Obstacle.class);

         Actor newEntity = this;
         if (move(world, target))
         {
            newEntity = this.tryTransform(world);
         }

         scheduleAction(world, newEntity,
                 newEntity.createAction(world, imageStore),
                 ticks + newEntity.getRate());
      };
      return action[0];
   }

   protected Miner transform(WorldModel world)
   {
      return new MinerNotFull(getName(), getPosition(), getRate(),
              getAnimationRate(), getResourceLimit(), getImages());
   }

   protected boolean move(WorldModel world, WorldEntity obstacle)
   {
      if (obstacle == null)
      {
         return false;
      }

      if (adjacent(getPosition(), obstacle.getPosition()))
      {
         setResourceCount(0);
         return true;
      }
      else
      {
         world.moveEntity(this, nextPosition(world, obstacle.getPosition()));
         return false;
      }
   }
}
