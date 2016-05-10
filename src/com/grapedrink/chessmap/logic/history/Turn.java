package com.grapedrink.chessmap.logic.history;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import com.grapedrink.chessmap.logic.bitboards.InputValidation;

public class Turn {

	private boolean a1 = true;
	private boolean h1 = true;
	private boolean a8 = true;
	private boolean h8 = true;
	
	private String src;
	private String dst;
	Map<String, Long> pieceDiffs;
	
	public Turn(String src, String dst) {
		InputValidation.validatePosition(src);
		InputValidation.validatePosition(dst);
		this.src = src;
		this.dst = dst;
		pieceDiffs = new HashMap<>();
	}
	
	public void addDiff(String pieceCode, long positions) {
		InputValidation.validatePieceCode(pieceCode);
		pieceDiffs.put(pieceCode, positions);
	}

	public String getSrc() {
		return src;
	}

	public String getDst() {
		return dst;
	}
	
	public Map.Entry<String,String> getMove() {
		return new AbstractMap.SimpleEntry<String, String>(src, dst);
	}
	
	public Map<String, Long> getDiffs() {
		return pieceDiffs;
	}

	public boolean getA1() {
		return a1;
	}
	
	public boolean getA8() {
		return a8;
	}
	
	public boolean getH1() {
		return h1;
	}
	
	public boolean getH8() {
		return h8;
	}

	public void setA1() {
		a1 = true;
	}
	
	public void setA8() {
		a8 = true;
	}
	
	public void setH1() {
		h1 = true;
	}

	public void setH8() {
		h8 = true;
	}
}
