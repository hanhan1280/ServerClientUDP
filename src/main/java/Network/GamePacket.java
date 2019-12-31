package Network;

public class GamePacket extends Packet{


    private String username;
    private int xPos;
    private int yPos;
    private int currentDir;
    private boolean isAttack;
    private boolean isMoving;


    public GamePacket(byte[] data){
        super(02);
        String[] dataMessage = new String(data).trim().split("#");
        this.username = dataMessage[1];
        this.xPos = Integer.parseInt(dataMessage[2]);
        this.yPos = Integer.parseInt(dataMessage[3]);
        this.currentDir = Integer.parseInt(dataMessage[4]);
        this.isAttack = Integer.parseInt(dataMessage[5]) == 1;
        this.isMoving = Integer.parseInt(dataMessage[6]) == 1;
    }

    public GamePacket(String username, int xPos, int yPos, int currentDir, boolean isAttack, boolean isMoving) {
        super(02);
        this.username = username;
        this.currentDir = currentDir;
        this.xPos =xPos;
        this.yPos = yPos;
        this.isAttack = isAttack;
        this.isMoving = isMoving;
    }

    @Override
    public void writeData(Client client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(Server server) {
        server.sendDataFull(getData());
    }

    @Override
    public byte[] getData() {
        return  ("" + id + "#" + this.username + "#" + this.xPos + "#" + this.yPos + "#" + this.currentDir + "#" + (this.isAttack? 1: 0) + "#" + (this.isMoving? 1:0) ).getBytes();
    }

    public String getUsername() {
        return username;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getCurrentDir() {
        return currentDir;
    }

    public boolean isAttack() {
        return isAttack;
    }

    public boolean isMoving() {
        return isMoving;
    }


}
