package a2;

public class PositiveTweetVisitor implements Visitor 
{
    @Override
    public double visit(TwitterService service) 
    {
        return service.getPositiveTweetPercentage();
    }
}
