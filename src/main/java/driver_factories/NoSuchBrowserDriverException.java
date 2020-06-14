package driver_factories;

public class NoSuchBrowserDriverException extends Exception{
    public NoSuchBrowserDriverException(String description){
        super(description);
    }
}
