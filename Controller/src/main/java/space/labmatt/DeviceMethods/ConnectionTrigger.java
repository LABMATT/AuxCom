package space.labmatt.DeviceMethods;

public class ConnectionTrigger {

    static boolean activeConnection = false;

    /**
     * If a a thead is currenly awating a connection then ture.
     * WHen a thread is connected to then trigger false.
     * @return
     */
    public synchronized boolean isActiveConnection() {

        return activeConnection;
    }

    /**
     * Chage the state of the active connection.
     * @param state
     */
    public synchronized void setActiveConnection(boolean state) {

        activeConnection = state;
    }
}
