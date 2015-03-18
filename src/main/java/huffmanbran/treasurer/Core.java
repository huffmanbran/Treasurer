package huffmanbran.treasurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

	public PluginDescriptionFile pdf;
	private Location l;
	private Chest c;
	private World w;
	private String xcord,ycord,zcord,message,prefix,wn;
	private Logger log;
	private Random r;
	private ItemStack[] treasureContents;
	private int offsetX,offsetY,offsetZ;
	private static int taskid = 0;
	private boolean allowTimer;
	protected static long delay;
	
	public Permission treasureBuy = new Permission("treasure.buy");
	public Permission treasureExact = new Permission("treasure.sendexact");
	public Permission treasureDeploy = new Permission("treasure.deploy");
	public Permission treasureReload = new Permission("treasure.reload");
	public Permission treasureMenu = new Permission("treasure.treasure");
	
	SettingsManager settings = SettingsManager.getInstance();

	public void onEnable() {

		
		settings.setup(this);
		saveDefaultConfig();
		getCommand("treasure").setExecutor(new Command(this));

		log = Bukkit.getLogger();
		pdf = this.getDescription();
		r = new Random();
		wn = settings.config.getString("world-name");
		delay = settings.config.getInt("timer-time-in-ticks");
		allowTimer = settings.config.getBoolean("allow-auto-spawn-chest");
		
		if(allowTimedChests() == false){
				
			Bukkit.getScheduler().cancelTask(taskid);
			log.info("Auto treasure has been set to false, disabling timer.");
				
			return;
		}
		
		Core.taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {

				makeRandomChest();

			}

		}, delay, delay);
		
		
		

	}

	public void onDisable() {
		
		
	}
	public boolean allowTimedChests(){
		
		if(allowTimer == false){
			
			return false;
			
		}
		
		return true;
	}
	/*Thanks for writing this method, Gary!*/
	public void addContents(){
		
	      
		
	      int amt = 0;

	      List<ItemStack> treasureList = new ArrayList<>();
	      Map<String, Object> configTreasure = settings.config
	    		  .getConfigurationSection( "treasure-contents" )
	    		  	.getValues( true );

	      for( Map.Entry<String, Object> treasure : configTreasure.entrySet() ){
	    	  
	    	  amt = r.nextInt( (Integer) treasure.getValue() - 0) + 0;
 
	    	  if(amt == 0){
	    		  
	    		  /*add nothing*/
	    		  
	    	  }else if(amt != 0){
	    		  
	    		 treasureList.add( new ItemStack( Material.matchMaterial( treasure.getKey() )
	    				 , amt ) );
	    		  
	    	  }

	      }

	      treasureContents = treasureList.toArray( new ItemStack[treasureList.size()] );
	
	}
	
	public void makeRandomChest(){
		
		addContents();
		message = settings.config.getString("broadcast-treasure-msg");
		prefix = settings.config.getString("prefix")
				.replaceAll("(&([a-f0-9]))", "\u00A7$2");
		w = Bukkit.getWorld(wn);
		
		int minX = settings.config.getInt("min-X");
		int minY = settings.config.getInt("min-Y");
		int minZ = settings.config.getInt("min-Z");
		
		int maxX = settings.config.getInt("max-X");
		int maxY = settings.config.getInt("max-Y");
		int maxZ = settings.config.getInt("max-Z");
		
		offsetX = settings.config.getInt("offset-X");
		offsetY = settings.config.getInt("offset-Y");
		offsetZ = settings.config.getInt("offset-Z");
		
		
		int x = r.nextInt(maxX - minX) + minX;
		int y = r.nextInt(maxY - minY) + minY;
		int z = r.nextInt(maxZ - minZ) + minZ;
		
		offsetX = x - offsetX;
		
		offsetY = y - offsetY;
		
		if(offsetY <= 0 || offsetY > 256){
			
			Bukkit.broadcastMessage(ChatColor.RED + 
					"Error! offset-Y is less than or equal to 0 or above 256, which is impossible.");
			
		}
		offsetZ = z - offsetZ;
		
		
		xcord = Integer.toString(offsetX);
		ycord = Integer.toString(offsetY);
		zcord = Integer.toString(offsetZ);
		
		
		
		l = new Location(w, x, y, z);
		
		
		l.getBlock().setType(Material.CHEST);
		
		c = (Chest) l.getBlock().getState();
		
		
		//announce treasure chest has been created
		
		Bukkit.broadcastMessage(prefix + settings.config.getString("broadcast-treasure-msg")
				.replaceAll("<worldname>", wn)
				.replaceAll("<xcord>", xcord)
				.replaceAll("<ycord>", ycord)
				.replaceAll("<zcord>", zcord)
				.replaceAll("(&([a-f0-9]))", "\u00A7$2"));
		
		for(int i = 0; i<treasureContents.length;i++){
			
			if(treasureContents[i]!=null){
				
				c.getInventory().addItem(treasureContents[i]);
				
			}
		}	
	}
	
}
