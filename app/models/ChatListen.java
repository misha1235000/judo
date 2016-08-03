package models;

public class ChatListen {
	private int msgfrom;
	private int msgto;
	private int amount;
	
	public ChatListen(int msgfrom, int msgto, int amount) {
		super();
		this.msgfrom = msgfrom;
		this.msgto = msgto;
		this.amount = amount;
	}
	
	public int getMsgfrom() {
		return msgfrom;
	}
	public void setMsgfrom(int msgfrom) {
		this.msgfrom = msgfrom;
	}
	public int getMsgto() {
		return msgto;
	}
	public void setMsgto(int msgto) {
		this.msgto = msgto;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
}