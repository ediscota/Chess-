package src.it.univaq.disim.datamodel.piece;

import lombok.Data;
import  src.it.univaq.disim.datamodel.board.Board;

@Data public abstract class Piece {
	
	protected int xCord;
	
	protected int yCord;
	
	protected Color color;

}