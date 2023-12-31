package org.firstinspires.ftc.teamcode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.LasagnaHardware;
import org.firstinspires.ftc.teamcode.Hardware.LasagnaIDS;

public class AutoUtil {
    private LasagnaHardware hardware;
    public AutoUtil(LasagnaHardware hardware){
        this.hardware = hardware;
    }
    public void waitTime(int waitTime){
        ElapsedTime time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        time.reset();
        while(time.time()<waitTime){}
    }
    public void flywheelPower(double power){
        hardware.flyWheelMotor.setPower(power);
    }
    public void clearServo(){
        hardware.pushServo.setPosition(0.5);
    }
    public void launch(){
        hardware.pushServo.setPosition(1.0);
        waitTime(300);
        hardware.pushServo.setPosition(0.5);
        hardware.flyWheelMotor.setPower(0.0);
    }
    public void launch(int num){
        for(int i = num; i>0; i--){
            hardware.pushServo.setPosition(1.0);
            waitTime(500);
            hardware.pushServo.setPosition(0.5);
            waitTime(1000);
        }
        hardware.flyWheelMotor.setPower(0.0);
    }
    public void intake(boolean yn){
       if(yn){
           hardware.intakeMotor.setPower(-1);
       }
       else
           hardware.intakeMotor.setPower(0.0);
    }
    public void setLaunchAngle(int time){
        ElapsedTime atime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        atime.reset();
        if(atime.time() < time){
            hardware.liftMotor.setPower(0.8);
        }
        hardware.liftMotor.setPower(0.0);
    }

}
