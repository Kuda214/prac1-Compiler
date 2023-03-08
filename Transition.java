/**
 * Transitions
 */
public class Transition {

    private String transitionValue;
    private State transitionTo;
    private State transitionFrom;

    public Transition(String transitionValue) {
        this.transitionValue = transitionValue;
      
    }

    public String getTransitionValue() {
        return transitionValue;
    }

    public State getTransitionTo() {
        return transitionTo;
    }

    public State getTransitionFrom() {
        return transitionFrom;
    }

    public void setTransitionValue(String transitionValue) {
        this.transitionValue = transitionValue;
    }

    public void setTransitionTo(State transitionTo) {
        this.transitionTo = transitionTo;
    }

    public void setTransitionFrom(State transitionFrom) {
        this.transitionFrom = transitionFrom;
    }

    

    

    //toString
    @Override
    public String toString() {
        return "Transition [transitionValue=" + transitionValue + ", transitionTo=" + transitionTo.getNodeLabel() + ", transitionFrom="
                + transitionFrom.getNodeLabel() + "]";
    }
   
}