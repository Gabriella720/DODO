package toolBox.service;

public class ServiceParameters {

	private int port;
	public ServiceParameters(int p)
	{
		port = p;
	}
	public ServiceParameters()
	{
		port = 2047;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
