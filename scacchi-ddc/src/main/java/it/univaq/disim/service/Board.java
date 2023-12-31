package it.univaq.disim.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import it.univaq.disim.datamodel.Bishop;
import it.univaq.disim.datamodel.Color;
import it.univaq.disim.datamodel.King;
import it.univaq.disim.datamodel.Knight;
import it.univaq.disim.datamodel.Move;
import it.univaq.disim.datamodel.Pawn;
import it.univaq.disim.datamodel.Piece;
import it.univaq.disim.datamodel.Queen;
import it.univaq.disim.datamodel.Rook;
import lombok.Data;

@Data
public class Board implements Cloneable, Serializable {

	private static final long serialVersionUID = -5189619093542858077L;
	private static final int columnsNumber = 8;
	private static final int linesNumber = 8;

	private Piece[][] board = new Piece[8][8];
	private List<Move> getAvailableMovesByColor;

	public static int getLinesnumber() {
		return linesNumber;
	}

	public static int getColumnsnumber() {
		return columnsNumber;
	}

	/** prende in input una posizione e controlla se è valida */
	public boolean isValidLocation(int x, int y) {

		if (x < 0 || x >= Board.columnsNumber || y < 0 || y >= Board.linesNumber)
			return false;
		else
			return true;

	}

	/**
	 * richiede le coordinate, e restituisce il pezzo in quella posizione
	 * 
	 * @return pezzo alla posizione selezionata
	 * @throws IllegalArgumentException
	 */
	public Piece getPieceAt(int x, int y) throws IllegalArgumentException {

		if (isValidLocation(x, y))
			return board[x][y];

		else
			throw new IllegalArgumentException("posizione non valida");

	}

	/**
	 * Inizializza la scacchiera posizionando ogni pezzo nella posizione giusta
	 */
	public void initializeBoard() {
		// Inizializza la scacchiera posizionando i pezzi correttamente
		board[0][0] = new Rook(Color.NERO, 0, 0, 5);
		board[0][1] = new Knight(Color.NERO, 0, 1, 3);
		board[0][2] = new Bishop(Color.NERO, 0, 2, 3);
		board[0][3] = new Queen(Color.NERO, 0, 3, 9);
		board[0][4] = new King(Color.NERO, 0, 4, 0);
		board[0][5] = new Bishop(Color.NERO, 0, 5, 3);
		board[0][6] = new Knight(Color.NERO, 0, 6, 3);
		board[0][7] = new Rook(Color.NERO, 0, 7, 5);

		for (int i = 0; i < getColumnsnumber(); i++) {
			board[1][i] = new Pawn(Color.NERO, 1, i, 1);
		}

		board[7][0] = new Rook(Color.BIANCO, 7, 0, 5);
		board[7][1] = new Knight(Color.BIANCO, 7, 1, 3);
		board[7][2] = new Bishop(Color.BIANCO, 7, 2, 3);
		board[7][3] = new Queen(Color.BIANCO, 7, 3, 9);
		board[7][4] = new King(Color.BIANCO, 7, 4, 3);
		board[7][5] = new Bishop(Color.BIANCO, 7, 5, 3);
		board[7][6] = new Knight(Color.BIANCO, 7, 6, 3);
		board[7][7] = new Rook(Color.BIANCO, 7, 7, 5);

		for (int i = 0; i < getColumnsnumber(); i++) {
			board[6][i] = new Pawn(Color.BIANCO, 6, i, 1);
		}

		// Inizializza il resto della scacchiera con pezzi vuoti
		for (int i = 2; i < 6; i++) {
			for (int j = 0; j < getColumnsnumber(); j++) {
				board[i][j] = null;
			}
		}
	}

	/** Stampa sulla console la scacchiera */
	public void displayBoard() {

		System.out.println("    0    1   2    3    4    5    6    7");
		System.out.println("  --------------------------------------  ");
		for (int i = 0; i < Board.linesNumber; i++) {
			System.out.print((0 + i) + "|");
			for (int j = 0; j < Board.columnsNumber; j++) {
				Piece piece = board[i][j];
				if (piece == null) {
					System.out.print("    ");
				} else {
					if (i > 0 && i < 7) {
						System.out.print(piece + "" + j);
					} else {
						if (j > 4) {
							System.out.print(piece + "2");
						} else {
							System.out.print(piece + "1");
						}

					}
				}
				System.out.print(" ");
			}
			System.out.println("|" + (0 + i));
		}
		System.out.println("  --------------------------------------  ");
		System.out.println("    0    1   2    3    4    5    6    7");
	}

	/**
	 * restituisce l'elenco dei pezzi nella scacchiera di un dato colore
	 */
	public List<Piece> getPiecesByColor(Color playerColor) {
		List<Piece> piecesByColor = new ArrayList<>();
		for (int x = 0; x < Board.columnsNumber; x++) {
			for (int y = 0; y < Board.linesNumber; y++) {
				Piece piece = board[x][y];
				if (piece != null && piece.getColor() == playerColor) {
					piecesByColor.add(piece);
				}
			}
		}

		return piecesByColor;
	}

	/**
	 * Applica la mossa da eseguire, aggiornando lo stato della scacchiera
	 */
	public void applyMove(Move move) {
		int startX = move.getStartXCord();
		int startY = move.getStartYCord();
		int endX = move.getEndXCord();
		int endY = move.getEndYCord();
		Piece pieceToMove = board[startX][startY];
		Piece destinationPiece = board[endX][endY];
		board[startX][startY] = null; // rimuovi dalla posizione di partenza il pezzo
		board[endX][endY] = pieceToMove;
		pieceToMove.setXCord(endX);
		pieceToMove.setYCord(endY);
		// Se la mossa è una mossa di cattura, rimuovi il pezzo catturato dalla
		// scacchiera
		if (destinationPiece != null) {
			destinationPiece.setXCord(-1); // Rimuovi il pezzo catturato dalla scacchiera impostando le coordinate a -1
			destinationPiece.setYCord(-1);
			destinationPiece.setValue(0); // Setta il valore del pezzo catturato a 0
		}
	}

	/**
	 * Restituisce la lista di tutte le mosse disponibili di un giocatore di un
	 * determinato colore
	 */
	public List<Move> getAvailableMovesByColor(Color color) {
		List<Move> availableMoves = new ArrayList<>();

		for (int x = 0; x < Board.columnsNumber; x++) {
			for (int y = 0; y < Board.linesNumber; y++) {
				Piece piece = board[x][y];
				if (piece != null && piece.getColor() == color) {
					List<Move> pieceMoves = piece.getAvailableMoves(this, x, y);
					availableMoves.addAll(pieceMoves);
				}
			}
		}

		return availableMoves;
	}

	/**
	 * resituisce true se il re è sotto scacco
	 * 
	 * @param color
	 * @param board
	 * @return true o false
	 */
	public boolean isKingInCheck(Color color, Board board) {
		// inizializzazione delle coordinate del re al di fuori della scacchiera
		int kingX = -1;
		int kingY = -1;
		boolean kingFound = false;
		boolean isInCheck = false;
		// doppio ciclo per trovare le coordinate del re
		for (int x = 0; x < Board.columnsNumber && !kingFound; x++) {
			for (int y = 0; y < Board.linesNumber && !kingFound; y++) {
				Piece piece = board.getPieceAt(x, y);
				if (piece instanceof King && piece.getColor() == color) {
					kingX = x;
					kingY = y;
					kingFound = true;
				}
			}
		}
		if (!kingFound)
			return false;
		// Controlla se il Re è minacciato da mosse avversarie
		List<Move> opponentMoves = board.getAvailableMovesByColor(color.oppositeColor());
		for (Move move : opponentMoves) {
			if (move.getEndXCord() == kingX && move.getEndYCord() == kingY) {
				isInCheck = true;
				break;
			}

		}
		return isInCheck;
	}

	/**
	 * Controlla se c'è lo scacco matto
	 */
	public boolean isCheckMate(Color color, Board board) {
		// se il re non è sotto scacco non può esserci scacco matto
		if (!isKingInCheck(color, board)) {
			return false;
		}
		// ottieni tutte le mosse dei pezzi dello stesso colore del re
		List<Move> alliesMoves = getAvailableMovesByColor(color);
		for (Move move : alliesMoves) {
			Board cloneBoard = this.clone();
			cloneBoard.applyMove(move);
			if (!cloneBoard.isKingInCheck(color, cloneBoard)) {
				// Il re può muoversi in una casella sicura, quindi non c'è scacco matto
				return false;
			}
		}
		return true;
	}

	@Override
	public Board clone() {
		try {
			Board cloneBoard = (Board) super.clone();

			// Clona l'array bidimensionale di Piece
			cloneBoard.board = new Piece[columnsNumber][linesNumber];
			for (int i = 0; i < columnsNumber; i++) {
				for (int j = 0; j < linesNumber; j++) {
					if (this.board[i][j] != null) {
						cloneBoard.board[i][j] = this.board[i][j].clone();
					}
				}
			}

			return cloneBoard;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;

		}
	}
}
