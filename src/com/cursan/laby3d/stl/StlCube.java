package com.cursan.laby3d.stl;

public class StlCube {
	private StlPoint begin;
	private StlPoint end;

	public StlCube() {
		super();
	}

	public StlCube(StlPoint begin, StlPoint end) {
		super();
		this.begin = begin;
		this.end = end;
	}

	public StlPoint getBegin() {
		return begin;
	}

	public void setBegin(StlPoint begin) {
		this.begin = begin;
	}

	public StlPoint getEnd() {
		return end;
	}

	public void setEnd(StlPoint end) {
		this.end = end;
	}

	public String generateStlFacet(StlPoint p1, StlPoint p2, StlPoint p3) {
		String facet = new String();
		facet += "facet normal 0 0 0" + System.lineSeparator();
		facet += "outer loop" + System.lineSeparator();
		facet += "vertex " + p1.getX() + " " + p1.getY() + " " + p1.getZ() + System.lineSeparator();
		facet += "vertex " + p2.getX() + " " + p2.getY() + " " + p2.getZ() + System.lineSeparator();
		facet += "vertex " + p3.getX() + " " + p3.getY() + " " + p3.getZ() + System.lineSeparator();
		facet += "endloop" + System.lineSeparator();
		facet += "endfacet" + System.lineSeparator();
		return facet;
	}

	public String generateStlFacets() {
		String content = new String();
		// bottom
		content += generateStlFacet(
				new StlPoint(begin.getX(), begin.getY(), begin.getZ()), 
				new StlPoint(end.getX(), begin.getY(), begin.getZ()),
				new StlPoint(begin.getX(), begin.getY(), end.getZ()));
		content += generateStlFacet(
				new StlPoint(end.getX(), begin.getY(), end.getZ()), 
				new StlPoint(begin.getX(), begin.getY(), end.getZ()),
				new StlPoint(end.getX(), begin.getY(), begin.getZ()));
		// top
		content += generateStlFacet(
				new StlPoint(begin.getX(), end.getY(), begin.getZ()), 
				new StlPoint(begin.getX(), end.getY(), end.getZ()),
				new StlPoint(end.getX(), end.getY(), begin.getZ()));
		content += generateStlFacet(
				new StlPoint(end.getX(), end.getY(), end.getZ()), 
				new StlPoint(end.getX(), end.getY(), begin.getZ()),
				new StlPoint(begin.getX(), end.getY(), end.getZ()));
		// left
		content += generateStlFacet(
				new StlPoint(begin.getX(), begin.getY(), begin.getZ()),
				new StlPoint(begin.getX(), begin.getY(), end.getZ()),
				new StlPoint(begin.getX(), end.getY(), end.getZ()));
		content += generateStlFacet(
				new StlPoint(begin.getX(), end.getY(), end.getZ()), 
				new StlPoint(begin.getX(), end.getY(), begin.getZ()),
				new StlPoint(begin.getX(), begin.getY(), begin.getZ()));
		// right
		content += generateStlFacet(
				new StlPoint(end.getX(), begin.getY(), begin.getZ()),
				new StlPoint(end.getX(), end.getY(), end.getZ()),
				new StlPoint(end.getX(), begin.getY(), end.getZ()));
		content += generateStlFacet(
				new StlPoint(end.getX(), end.getY(), end.getZ()), 
				new StlPoint(end.getX(), begin.getY(), begin.getZ()),
				new StlPoint(end.getX(), end.getY(), begin.getZ()));
		// in front
		content += generateStlFacet(
				new StlPoint(begin.getX(), begin.getY(), begin.getZ()), 
				new StlPoint(begin.getX(), end.getY(), begin.getZ()),
				new StlPoint(end.getX(), begin.getY(), begin.getZ()));
		content += generateStlFacet(
				new StlPoint(end.getX(), end.getY(), begin.getZ()), 
				new StlPoint(end.getX(), begin.getY(), begin.getZ()),
				new StlPoint(begin.getX(), end.getY(), begin.getZ()));
		// behind
		content += generateStlFacet(
				new StlPoint(begin.getX(), begin.getY(), end.getZ()), 
				new StlPoint(end.getX(), begin.getY(), end.getZ()),
				new StlPoint(begin.getX(), end.getY(), end.getZ()));
		content += generateStlFacet(
				new StlPoint(end.getX(), end.getY(), end.getZ()), 
				new StlPoint(begin.getX(), end.getY(), end.getZ()),
				new StlPoint(end.getX(), begin.getY(), end.getZ()));
		return content;
	}
}
