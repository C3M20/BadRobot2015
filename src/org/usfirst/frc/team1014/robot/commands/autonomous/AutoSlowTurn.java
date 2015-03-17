package org.usfirst.frc.team1014.robot.commands.autonomous;
import org.usfirst.frc.team1014.robot.commands.CommandBase;

import edu.wpi.first.wpilibj.Utility;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * This is the turning command for autonomous.
 * 
 * @author Manu S.
 *
 */
public class AutoSlowTurn extends CommandBase {

	public double degree;
	public double difference;	
	
	public double startTime, passedTime;
	
	public AutoSlowTurn(double degree)
	{
		requires((Subsystem) driveTrain);
		this.degree = degree;
	}
	
	@Override
	protected void initialize() {
		driveTrain.tankDrive(0, 0);
		this.difference = 0;
		this.passedTime = 0;
		this.startTime = Utility.getFPGATime();
		driveTrain.resetGyro();	
		//driveTrain.resetPwmGyro();		
	}

	@Override
	public String getConsoleIdentity() {
		return "AutoTurn";
	}

	@Override
	protected void execute() {
		passedTime = Utility.getFPGATime() - startTime;
		difference = driveTrain.getAngle() - degree;// negative for counterclockwise
		driveTrain.mecanumDrive(0, 0, deadzone(rotation()));
		
	}

	@Override
	protected boolean isFinished() {
		if(deadzone(difference) == 0 || passedTime/1000000 > 2)
		{
			System.out.println("AutoTurnFinished");
			return true;
		}

		return false;
	}

	@Override
	protected void end() {
		driveTrain.tankDrive(0, 0);
		
	}

	@Override
	protected void interrupted() {
		System.out.println("AutoTurn interuppted");
		
	}
	
	public double rotation()
	{
		return -0.4;//return -(difference/180);//higher divisor for slower speed
	}
    public static double deadzone(double d) {
        //whenever the controller moves LESS than the magic number, the 
        //joystick is in the loose position so return zero - as if the 
        //joystick was not moved
        if (Math.abs(d) < .1) {
            return 0;
        }
        
        if (d == 0)
        {
            return 0;
        }
        //When the joystick is used for a purpose (passes the if statements, 
        //hence not just being loose), do math
        return (d / Math.abs(d)) //gets the sign of d, negative or positive
            * ((Math.abs(d) - .1) / (1 - .1)); //scales it
    }

}
