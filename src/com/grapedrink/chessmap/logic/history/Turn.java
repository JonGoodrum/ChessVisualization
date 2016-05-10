package com.grapedrink.chessmap.logic.history;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import com.grapedrink.chessmap.logic.bitboards.InputValidation;

public class Turn {

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
}
