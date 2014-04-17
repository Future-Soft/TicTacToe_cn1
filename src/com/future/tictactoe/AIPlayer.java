package com.future.tictactoe;

import java.util.*;

import com.codename1.ui.*;

public class AIPlayer extends Player {
	
	public static final int DUMB = 0;
	public static final int EASY = 1;
	public static final int NORMAL = 2;
	public static final int HARD = 3;
	
	private static final int[] DEPTH = {0,2,3,4};
	
	private int counter;
	private GameController game;
	private int level;
	
	public AIPlayer(int num, int level, Board board, GameController game) {
		super(num, board);
		this.game = game;
		this.level = level;
	}
	
	public AIPlayer(int num, int level, GameController game){
		super(num);
		this.game = game;
		this.level = level;
	}

	@Override
	public void play() {
		new Thread(new Runnable() {
			
			public synchronized void run() {
				try {
					this.wait(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Move m;
				if(board.isEmpty()){
					Random rnd = new Random();
					m = new Move(rnd.nextInt(3), rnd.nextInt(3), AIPlayer.this);
				}
				else
					m = getMove();
				game.makeMove(m);
				
			}
		}).start();
	}

	protected Move getMove() {
		double rndPercent = 0;
		if(level == DUMB)
			rndPercent = 0.70;
		else if(level == EASY)
			rndPercent = 0.5;
		else if(level == NORMAL)
			rndPercent = 0.20;
		Random rnd = new Random();
		if((double)(rnd.nextInt(10))/10 < rndPercent){
			int i = rnd.nextInt(3);
			int j = rnd.nextInt(3);
			Move m = new Move(i, j, this);
			while(!board.isValidMove(m)){
				i = rnd.nextInt(3);
				j = rnd.nextInt(3);
				m = new Move(i, j, this);
			}
			return m;
		}
		counter = 0;
    	int chosenMove=-1;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        Vector exp=new Vector(3);
        Vector moves=new Vector();
        int expSize,movesSize;
        for (int i=0;i<3;++i) {
        	for(int j=0; j<3; j++){
	    		Move m=new Move(i, j, this);
	    		if (board.isValidMove(m)) {
	    			Board bb= new Board(board);
	    			//System.out.println(bb.toString());
	    			bb.updateMove(m);
	    			exp.addElement(bb);
	    			moves.addElement(m);
	    		}
        	}
    	}
    	expSize = exp.size();
    	movesSize = moves.size();
        for (int i=0; i<expSize;++i){
        	if  (((Board) exp.elementAt(i)).checkForWin()!=null){
        		return (Move)moves.elementAt(i);
            }
        }
        for (int i=0; i<expSize;i++){
        	int temp=Min((Board)exp.elementAt(i),0,alpha,beta);
        	if (temp>alpha) {
        		chosenMove=i;
        		alpha=temp;
        	}
        	/*else
        		if (temp==alpha && System.currentTimeMillis() % 3 == 0)
        		{
        			chosenMove=i;
            		alpha=temp;
        		}*/
        		
        }
        if (chosenMove!=-1)
        	return (Move)moves.elementAt(chosenMove);
        else {
        	for (int i=0; i<expSize;i++) 
        		for (int j=0; j<3;++j){
        			for(int k=0; k<3; k++){
		        		Move m=new Move(j, k, this);
		        		Board bb = new Board((Board)exp.elementAt(i));
		        		if (bb.isValidMove(m)) {
		        			bb.updateMove(m);
		        			if (bb.checkForWin()!=null)
		        				return m;
		        		}
        			}
	        	}
        	int temp=Integer.MIN_VALUE;
        	chosenMove=0;
        	for (int i=0;i<movesSize;i++)
        		if (huristic((Board)exp.elementAt(i),true)>=temp) {
        			chosenMove=i;
        			temp=huristic((Board)exp.elementAt(i),true);
        		}
        	return (Move)moves.elementAt(chosenMove);
        }
	}

	public int Max(Board b,int depth,int alpha,int beta){
        if (depth> (DEPTH[2])){
            return huristic(b,true);
        }
        //Thread.yield();
        //System.out.println(counter); 
        /*if ((counter++) == 30)
        try
		{
        	counter = 0;
			Thread.sleep(5);
		} catch (InterruptedException e)
		{}*/
        Vector sons = expand(b,true);
        for (int i=0; i<sons.size();i++){
        	if  (((Board) sons.elementAt(i)).checkForWin()!=null){
        		return Integer.MAX_VALUE;
            }
        }
        for (int i=0; i<sons.size();i++){
            if  (((Board) sons.elementAt(i)).isFull()){
            	return 0;
            }
        }
        for (int i=0; i<sons.size();i++){
        	alpha = Math.max(Min((Board)sons.elementAt(i),depth+1,alpha,beta),alpha);
            if (alpha>=beta){
                return beta;
            }
        }
        return alpha;
    }
    public int Min(Board b,int depth,int alpha,int beta){
        if ((depth > (DEPTH[2])) ){
            return huristic(b,false);
        }
        Vector sons = expand(b,false);
        for (int i=0; i<sons.size();i++){
        	if  (((Board) sons.elementAt(i)).checkForWin()!=null){
        		return Integer.MIN_VALUE;
            }
        }
        for (int i=0; i<sons.size();i++){
            if  (((Board) sons.elementAt(i)).isFull()){
            	return 0;
            }
        }
        for (int i=0; i<sons.size();i++){
        	beta = Math.min(Max((Board)sons.elementAt(i),depth+1,alpha,beta),beta);
            if (alpha>=beta){
                return alpha;
            }
        }
        return beta;
    }
    public Vector expand(Board b,boolean aiplayer) {
    	int cols = 3;
    	Vector exp=new Vector(cols);
    	Player p;
    	if(aiplayer)
    		p=this;
    	else{
    		Player[] players = game.getPlayers();
    		if(players[0] == this)
    			p = players[1];
    		else
    			p = players[0];
    	}
    	Move m;
    	for (int i=0;i<cols;i++) {
    		for(int j=0; j<3; j++){
	    		m = new Move(i, j, p);
	    		if (b.isValidMove(m)) {
	    			Board bb= new Board(b);
	    			//System.out.println(bb.toString());
	    			bb.updateMove(m);
	    			exp.addElement(bb);
	    		}
    		}
    	}
    	return exp;
    }
    public int huristic(Board b,boolean aiplayer) {
    	int[][][] stast=b.getStat();
    	if (b.checkForWin()!=null)
    		return (aiplayer?Integer.MAX_VALUE:Integer.MIN_VALUE);
    	int mystat=(stast[stast.length-1][1][0]*2)+(stast[stast.length-1][1][1]*3);
    	int hisstat=(stast[stast.length-1][0][0]*2)+(stast[stast.length-1][0][1]*3);
    	return (!aiplayer?mystat-hisstat:hisstat-mystat);
    }

}
