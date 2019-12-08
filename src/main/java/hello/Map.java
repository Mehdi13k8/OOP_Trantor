import java.util.*;

public class Map {
    int food = 0;
    int thystame = 0;
    int mendiane = 0;
    int phiras = 0;
    int sibur = 0;
    int deraumere = 0;
    int linemate = 0;
    int repeat = (int)(Math.random()*7);
    int rnd = 0;
    int playernb = 0;

    public Map() {
	while (repeat != 0) {
	    rnd = (int)(Math.random()*7);
	    switch (rnd) {
	    case 0:
		food++;
		break;
	    case 1:
		thystame++;
		break;
	    case 2:
		mendiane++;
		break;
	    case 3:
		phiras++;
		break;
	    case 4:
		sibur++;
		break;
	    case 5:
		deraumere++;
		break;
	    case 6:
		linemate++;
		break;
	    default:
		break;
	    }
	    repeat --;
	}
    }
}
