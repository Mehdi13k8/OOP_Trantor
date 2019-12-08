import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Trantor extends Map {
    int port = 0;
    int width = 0;
    int height = 0;
    ArrayList<String> nameX = new ArrayList<String>();
    int clientNB = 0;;
    int frequence = 0;
    int closed = 0;
    ServerSocket serverSocket = null;
    int playerx = 0;
    int playery = 0;
    Map[][] map;
    int food = 0;
    int thystame = 0;
    int mendiane = 0;
    int phiras = 0;
    int sibur = 0;
    int deraumere = 0;
    int linemate = 0;
    int rotation = 1;
    public Trantor() {
    }
    public void createmap(int width, int height) {
	map = new Map[height][width];
	for(int yy = 0; yy < height; yy++)
	    for(int xx = 0; xx < width; xx++)
	    {
		map[yy][xx] = new Map();
	    }
    }
    public static void main(String[] args) {
	Trantor trant = new Trantor();
	int flagp = 0;
	int flagx = 0;
	int flagy = 0;
	int flagn = 0;
	int flagc = 0;
	int flagf = 0;
	if (args.length == 1 && args[0].equals("-help")) {
	    System.out.print("USAGE: trantor.jar -p port -x width -y height");
	    System.out.print(" -n name1 name2 -c clientsNB -f freq\n");
	    System.out.println("\tport\tis the port number");
	    System.out.println("\twidth\tis the width of the world");
	    System.out.println("\theight\tis the height of the world");
	    System.out.println("\tnameX\tis the name of the team X");
	    System.out.println("\tclientsNB\tis the number of authorized clents per team");
	    System.out.println("\tfreq\tis the reciprocal of time unit for executions of actions");
	    System.exit(0);
	}
	else if (args.length == 1 && !args[0].equals("-help")) {
	    System.out.println("help issue");
	    System.exit(84);
	}
	int i = 0;
	if (args.length >= 12)
	    while (i < args.length) {
		if (args[i].equals("-p") && flagp == 0) {
		    flagp = 1;
		    if (args.length > i + 1)
			if (Integer.parseInt(args[i+1]) < 0) {
			    System.out.println("write correct port number");
			    System.exit(84);
			}
		    if (args[i+1] == null) 
			System.exit(84);
		    try {
			trant.port = Integer.parseInt(args[i+1]);
		    }
		    catch(NumberFormatException ex)
		    {
			System.exit(84);
		    }
		}
		else if (args[i].equals("-p") && flagx != 0)
		    System.exit(84);
		if (args[i].equals("-x") && flagx == 0) {
		    flagx = 1;
		    if (args.length > i + 1)
			if (Integer.parseInt(args[i+1]) < 0) {
			    System.out.println("write correct width number");
			    System.exit(84);
			}
		    if (args[i+1] == null) 
			System.exit(84);
		    try {
			trant.width = Integer.parseInt(args[i+1]);
		    }
		    catch(NumberFormatException ex)
		    {
			System.exit(84);
		    }
		}
		else if (args[i].equals("-x") && flagx != 0)
		    System.exit(84);
		
		if (args[i].equals("-y") && flagy == 0) {
		    flagy = 1;
		    
		    if (args[i+1] == null) 
			System.exit(84);
		    try {
			trant.height = Integer.parseInt(args[i+1]);
		    }
		    catch(NumberFormatException ex)
		    {
			System.exit(84);
		    }
		}
		else if (args[i].equals("-y") && flagx != 0)
		    System.exit(84);
		
		if (args[i].equals("-n") && flagn == 0) {
		    flagn = 1;
		    if (args[i+1] == null) 
			System.exit(84);
		    while (i < args.length && args[i+1].charAt(0) != '-') {
			if (args.length < i + 1)
			    break;
			if (args[i+1] == null) 
			    System.exit(84);
			if (args[i+1] != null && args[i+1].charAt(0) != '-')
			    trant.nameX.add(args[i+1]);
			i++;
		    }
		}
		else if (args[i].equals("-n") && flagn != 0)
		    System.exit(84);
		if (args[i].equals("-c") && flagc == 0) {
		    flagc = 1;
		    if (args[i+1] == null) 
			System.exit(84);
		    try {
			trant.clientNB = Integer.parseInt(args[i+1]);
		    }
		    catch(NumberFormatException ex) {
			System.exit(84);
		    }
		}
		else if (args[i].equals("-c") && flagc != 0)
		    System.exit(84);
		if (args[i].equals("-f") && flagf == 0) {
		    flagf = 1;
		    if (args.length <= i + 1) {
			System.exit(84);
		    }
		    if (args[i+1] == null) 
			System.exit(84);
		    try {
			trant.frequence = Integer.parseInt(args[i+1]);
		    }
		    catch(NumberFormatException ex) {
			System.exit(84);
		    }
		}
		else if (args[i].equals("-f") && flagf != 0)
		    System.exit(84);
		i++;
	    }
	if (flagp ==  0 || flagx == 0 || flagy == 0 || flagn == 0 || flagc == 0 || flagf == 0)
	    System.exit(84);
	trant.createmap(trant.width, trant.height);
	ExecutorService executor = null;
	try (ServerSocket sSocket = new ServerSocket(trant.port);) {
	    executor = Executors.newFixedThreadPool(12);
	    System.out.println("Port : " + trant.port);
	    while (true) {
		Socket clientsock = sSocket.accept();
		Runnable worker = new RequestHandler(clientsock, trant);
		executor.execute(worker);
	    }
	}
	catch (IOException e) {
	    System.err.println("Le port " + trant.port + " est déjà utilisé ! ");
	    System.exit(84);
	}
	System.out.println("gg");
    }
}

class RequestHandler implements Runnable {
    private final Socket client;
    ServerSocket serverSocket = null;
    private int inteam = 0;
    Trantor trant;
    int playerxx = 0;
    int playeryy = 0;
    int lvl = 1;

    public RequestHandler(Socket client, Trantor trant) {
	this.client = client;
	this.trant = trant;
	playerxx = (int)(Math.random()*trant.width);
	playeryy = (int)(Math.random()*trant.height);
    }
    @Override
    public void run() {
	try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	     BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));)
	    {
		String userInput;
		writer.write("WELCOME");
		writer.newLine();
		writer.flush();
		while ((userInput = in.readLine()) != null) {
		    if (inteam == 0) {
			for (String temp : trant.nameX) {
			    if (temp.equals(userInput)) {
				System.out.println(temp);
				trant.clientNB--;
				writer.write(trant.clientNB + "\n" + trant.width + " " + trant.height);
				writer.newLine();
				writer.flush();
				inteam++;
				trant.map[playeryy][playerxx].playernb++;
			    }
			}
			if (inteam == 0) {
			    writer.write("ko");
			    writer.newLine();
			    writer.flush();
			}
		    }
		    else {
			team_game(writer, userInput);
		    }
		}
	    } catch (IOException e) {
		System.out.println("I/O exception: " + e);
	    } catch (Exception ex) {
		System.out.println("Exceprion in Thread Run. Exception : " + ex);
	    }
	}
    public void team_game(BufferedWriter writer, String user) {
	switch (user) {
	case "inventory":
	    my_inventory(writer);
	    break;
	case "look":
	    my_look(writer);
	    break;
	case "forward":
	    my_forward(writer);
	    break;
	case "right":
	    my_right(writer);
	    break;
	case "left":
	    my_left(writer);
	    break;
	case "take food":
	    my_food_take(writer);
	    break;
	case "take thystame":
	    my_thystame_take(writer);
	    break;
	case "take mendiane":
	    my_mendiane_take(writer);
	    break;
	case "take phiras":
	    my_phiras_take(writer);
	    break;
	case "take sibur":
	    my_sibur_take(writer);
	    break;
	case "take deraumere":
	    my_deraumere_take(writer);
	    break;
	case "take linemate":
	    my_linemate_take(writer);
	    break;
	case "drop food":
	    my_food_drop(writer);
	    break;
	case "drop thystame":
	    my_thystame_drop(writer);
	    break;
	case "drop mendiane":
	    my_mendiane_drop(writer);
	    break;
	case "drop phiras":
	    my_phiras_drop(writer);
	    break;
	case "drop sibur":
	    my_sibur_drop(writer);
	    break;
	case "drop deraumere":
	    my_deraumere_drop(writer);
	    break;
	case "drop linemate":
	    my_linemate_drop(writer);
	    break;
	case "Broadcast text":
	    my_broadcast(writer);
	    break;
	case "Connect_nbr":
	    my_connect(writer);
	    break;
	case "Fork":
	    my_fork(writer);
	    break;
	case "Eject":
	    my_eject(writer);
	    break;
	case "Incantation":
	    my_incant(writer);
	    break;
	default:
	    try {
		writer.write("ko");
		writer.newLine();
		writer.flush();
	    }
	    catch (IOException e) {
		System.out.println("I/O exception: " + e);
	    } catch (Exception ex) {
		System.out.println("Exceprion in Thread Run. Exception : " + ex);
	    }
	    break;
	}
    }
    private void my_inventory(BufferedWriter writer) {
	String inv = "[food " + trant.food + ", thystame "+ trant.thystame+
	    ", mendiane " + trant.mendiane + ", phiras " + trant.phiras +
	    ", sibur " + trant.sibur + ", deraumere " + + trant.deraumere +
	    ", linemate " + trant.linemate + "]";
	try {
	    writer.write(inv);
	    writer.newLine();
	    writer.flush();
	}
	catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_look(BufferedWriter writer) {
	try {
	    writer.write("ok");
	    writer.newLine();
	    writer.flush();
	}
	catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_forward(BufferedWriter writer) {
	switch (trant.rotation) {
	case 1:
	    if (playeryy > 0) {
		trant.map[playeryy][playerxx].playernb--;
		playeryy = playeryy-1;
		trant.map[playeryy][playerxx].playernb++;
	    }
	    else {
		trant.map[playeryy][playerxx].playernb--;
		playeryy = trant.height-1;
		trant.map[playeryy][playerxx].playernb++;
	    }
	    break;
	case 2:
	    if (playerxx - trant.width-1 > 0) {
		trant.map[playeryy][playerxx].playernb--;
		playerxx = playerxx+1;
		trant.map[playeryy][playerxx].playernb++;
	    }
	    else {
		trant.map[playeryy][playerxx].playernb--;
		playerxx = 0;
		trant.map[playeryy][playerxx].playernb++;
	    }
	    break;
	case 3:
	    if (playeryy - trant.height-1 > 0) {
		trant.map[playeryy][playerxx].playernb--;
		playeryy = playeryy+1;
		trant.map[playeryy][playerxx].playernb++;
	    }
	    else {
		trant.map[playeryy][playerxx].playernb--;
		playeryy = trant.height-1;
		trant.map[playeryy][playerxx].playernb++;
	    }
	    break;
	case 4:
	    if (playerxx > 0) {
		trant.map[playeryy][playerxx].playernb--;
		playerxx = playerxx-1;
		trant.map[playeryy][playerxx].playernb++;
	    }
	    else {
		trant.map[playeryy][playerxx].playernb--;
		playerxx = trant.width-1;
		trant.map[playeryy][playerxx].playernb++;
	    }
	    break;
	default:
	    break;
	}
	try {
	    writer.write("ok");
	    writer.newLine();
	    writer.flush();
	}
	catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_left(BufferedWriter writer) {
	if (trant.rotation >= 1)
	    trant.rotation--;
	if (trant.rotation == 0)
	    trant.rotation = 4;
	try {
	    writer.write("ok");
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_right(BufferedWriter writer) {
	try {
	    if (trant.rotation <= 4)
		trant.rotation++;
	    if (trant.rotation == 5)
		trant.rotation = 1;
	    writer.write("ok");
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_food_take(BufferedWriter writer) {
	try {
	    if (trant.map[playeryy][playerxx].food != 0) {
		writer.write("ok");
		trant.map[playeryy][playerxx].food--;
		trant.food++;
	    }
	    else if (trant.map[playeryy][playerxx].food == 0) {
		writer.write("ko");
	    }
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_thystame_take(BufferedWriter writer) {
	try {
	    if (trant.map[playeryy][playerxx].thystame != 0) {
		writer.write("ok");
		trant.map[playeryy][playerxx].thystame--;
		trant.thystame++;
	    }
	    else if (trant.map[playeryy][playerxx].thystame == 0) {
		writer.write("ko");
	    }
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_mendiane_take(BufferedWriter writer) {
	try {
	    if (trant.map[playeryy][playerxx].mendiane != 0) {
		writer.write("ok");
		trant.map[playeryy][playerxx].mendiane--;
		trant.mendiane++;
	    }
	    else if (trant.map[playeryy][playerxx].mendiane == 0) {
		writer.write("ko");
	    }
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_phiras_take(BufferedWriter writer) {
	try {
	    if (trant.map[playeryy][playerxx].phiras != 0) {
		writer.write("ok");
		trant.map[playeryy][playerxx].phiras--;
		trant.phiras++;
	    }
	    else if (trant.map[playeryy][playerxx].phiras == 0) {
		writer.write("ko");
	    }
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_sibur_take(BufferedWriter writer) {
	try {
	    if (trant.map[playeryy][playerxx].sibur != 0) {
		writer.write("ok");
		trant.map[playeryy][playerxx].sibur--;
		trant.sibur++;
	    }
	    else if (trant.map[playeryy][playerxx].sibur == 0) {
		writer.write("ko");
	    }
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_deraumere_take(BufferedWriter writer) {
	try {
	    if (trant.map[playeryy][playerxx].deraumere != 0) {
		writer.write("ok");
		trant.map[playeryy][playerxx].deraumere--;
		trant.deraumere++;
	    }
	    else if (trant.map[playeryy][playerxx].deraumere == 0) {
		writer.write("ko");
	    }
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_linemate_take(BufferedWriter writer) {
	try {
	    if (trant.map[playeryy][playerxx].linemate != 0) {
		writer.write("ok");
		trant.map[playeryy][playerxx].linemate--;
		trant.linemate++;
	    }
	    else if (trant.map[playeryy][playerxx].linemate == 0) {
		writer.write("ko");
	    }
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_food_drop(BufferedWriter writer) {
	try {
	    if (trant.map[playeryy][playerxx].food > 0) {
		writer.write("ok");
		trant.map[playeryy][playerxx].food++;
		trant.food--;
	    }
	    else if (trant.food == 0) {
		writer.write("ko");
	    }
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_thystame_drop(BufferedWriter writer) {
	try {
	    if (trant.map[playeryy][playerxx].thystame > 0) {
		writer.write("ok");
		trant.map[playeryy][playerxx].thystame++;
		trant.thystame--;
	    }
	    else if (trant.thystame == 0) {
		writer.write("ko");
	    }
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_mendiane_drop(BufferedWriter writer) {
	try {
	    if (trant.map[playeryy][playerxx].mendiane > 0) {
		writer.write("ok");
		trant.map[playeryy][playerxx].mendiane++;
		trant.mendiane--;
	    }
	    else if (trant.mendiane == 0) {
		writer.write("ko");
	    }
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_phiras_drop(BufferedWriter writer) {
	try {
	    if (trant.map[playeryy][playerxx].phiras > 0) {
		writer.write("ok");
		trant.map[playeryy][playerxx].phiras++;
		trant.phiras--;
	    }
	    else if (trant.phiras == 0) {
		writer.write("ko");
	    }
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_sibur_drop(BufferedWriter writer) {
	try {
	    if (trant.map[playeryy][playerxx].sibur > 0) {
		writer.write("ok");
		trant.map[playeryy][playerxx].sibur++;
		trant.sibur--;
	    }
	    else if (trant.sibur == 0) {
		writer.write("ko");
	    }
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_deraumere_drop(BufferedWriter writer) {
	try {
	    if (trant.map[playeryy][playerxx].deraumere > 0) {
		writer.write("ok");
		trant.map[playeryy][playerxx].deraumere++;
		trant.deraumere--;
	    }
	    else if (trant.deraumere == 0) {
		writer.write("ko");
	    }
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_linemate_drop(BufferedWriter writer) {
	try {
	    if (trant.map[playeryy][playerxx].linemate > 0) {
		writer.write("ok");
		trant.map[playeryy][playerxx].linemate++;
		trant.linemate--;
	    }
	    else if (trant.linemate == 0) {
		writer.write("ko");
	    }
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_broadcast(BufferedWriter writer) {
	try {
	    writer.write("ok");
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_connect(BufferedWriter writer) {
	try {
	    writer.write(trant.clientNB);
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_fork(BufferedWriter writer) {
	try {
	    writer.write("ok");
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_eject(BufferedWriter writer) {
	try {
	    if (trant.map[playeryy][playerxx].playernb > 0)
		writer.write("ok");
	    else
		writer.write("ko");
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
    private void my_incant(BufferedWriter writer) {
	try {
	    if (lvl == 1) {
		if (trant.map[playeryy][playerxx].linemate > 0)
		    writer.write("ok");
		else
		    writer.write("ko");
	    }
	    else if (lvl == 2) {
		if (trant.map[playeryy][playerxx].linemate > 0 && trant.map[playeryy][playerxx].deraumere > 0 &&
		    trant.map[playeryy][playerxx].sibur > 0)
		    writer.write("ok");
		else
		    writer.write("ko");
	    }
	    else if (lvl == 3) {
		if (trant.map[playeryy][playerxx].linemate > 0 && trant.map[playeryy][playerxx].deraumere > 0 &&
		    trant.map[playeryy][playerxx].sibur > 0)
		    writer.write("ok");
		else
		    writer.write("ko");
	    }
	    else
		writer.write("ko");
	    writer.newLine();
	    writer.flush();
	} catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} catch (Exception ex) {
	    System.out.println("Exceprion in Thread Run. Exception : " + ex);
	}
    }
}
