package devices;

import events.Notification;
import events.NotificationType;
import creature.Person;
import house.Room;

public class Shower extends Device {

    private int actualUsingDuration = 0;
    private final static int maxUsingDuration = 3;

    public Shower(String name, String brand, Room location, ConsumptionType consumptionType, double[] consumption) {
        super(name, brand, location, consumptionType, consumption);
    }

    /**
     * Implements device using.
     *
     * @param person is a person that use device
     * @return using state
     *
     * @see Shower#takingAShowerNotify(Person)
     */
    @Override
    public Usable use(Person person) {
        setBusy(true);

        switch (getDeviceState()){
            case OFF:
            case IDLE:
                turnON();
                changeUsingOfDevice();
                return this;
            case ON:
                return useShower(person);
        }
        return null;
    }

    /**
     * Notify another housemates about person using device.
     *
     * @param person is person that using device
     */
    private void takingAShowerNotify(Person person) {
        newNotification(new Notification(NotificationType.TAKE_A_SHOWER, getActualFloor(), getActualRoom(), person,this));
    }

    /**
     * Controls device's condition.
     */
    @Override
    public void changeUsingOfDevice() {
        conditionOfDevice -= 4;
        if (conditionOfDevice < 0) {
            conditionOfDevice = 0;
            broken();
        }
        announce();
    }

    @Override
    public String toString() {
        return deviceName;
    }


    /**
     * Validates using of device when device's state is ON.
     *
     * @param person using device
     * @return using state
     */
    private Usable useShower(Person person){
        if (actualUsingDuration < maxUsingDuration) {
            if (actualUsingDuration == 0)
                takingAShowerNotify(person);
            actualUsingDuration++;
            return this;
        } else {
            actualUsingDuration = 0;
            setBusy(false);
            turnIDLE();
            return null;
        }
    }
}
