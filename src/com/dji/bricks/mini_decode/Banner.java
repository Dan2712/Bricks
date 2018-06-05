package com.dji.bricks.mini_decode;

public class Banner {
	
	private int version;
	private int length;
	private int pid;
	private int readWidth;
	private int readHeight;
	private int virtualWidth;
	private int virtualHeight;
	private int orientation;
	private int quirks;

	public int getLength() {
		return length;
	}

	public int getOrientation() {
		return orientation;
	}

	public int getPid() {
		return pid;
	}

	public int getQuirks() {
		return quirks;
	}

	public int getReadHeight() {
		return readHeight;
	}

	public int getReadWidth() {
		return readWidth;
	}

	public int getVersion() {
		return version;
	}

	public int getVirtualHeight() {
		return virtualHeight;
	}

	public int getVirtualWidth() {
		return virtualWidth;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public void setQuirks(int quirks) {
		this.quirks = quirks;
	}
	public void setReadHeight(int readHeight) {
		this.readHeight = readHeight;
	}
	public void setReadWidth(int readWidth) {
		this.readWidth = readWidth;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public void setVirtualHeight(int virtualHeight) {
		this.virtualHeight = virtualHeight;
	}
	public void setVirtualWidth(int virtualWidth) {
		this.virtualWidth = virtualWidth;
	}
	
	@Override
	public String toString() {
		return "Banner [version=" + version + ", length=" + length + ", pid="
				+ pid + ", readWidth=" + readWidth + ", readHeight="
				+ readHeight + ", virtualWidth=" + virtualWidth
				+ ", virtualHeight=" + virtualHeight + ", orientation="
				+ orientation + ", quirks=" + quirks + "]";
	}
}
