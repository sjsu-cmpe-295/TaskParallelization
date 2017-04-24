package sjsu.cmpe.B295.communicationMessageHandlers;



public interface IHttpRequestHandler {
	public void handleHttpRequest(String uri,Integer time);
	public void setNextHandler(IHttpRequestHandler nextHandler);
}
