package org.firstinspires.ftc.teamcode.Drive;

import org.firstinspires.ftc.teamcode.Hardware.LasagnaHardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp (name = "Lasagna")
public class Lasagna extends OpMode {
    LasagnaHardware hardware;
   
    public static final double FAST_MODE = .9;
    public static final double PREC_MODE = .45;

    double currentMode;
    ElapsedTime buttonTime = null;

    public void init(){
        hardware = new LasagnaHardware();
        hardware.init(hardwareMap);
        currentMode = FAST_MODE;
        buttonTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        telemetry.addData("Status: ", "Initialized");
        telemetry.update();
    }

    public void start(){
        telemetry.addData("Status: ", "Started");
        telemetry.update();
    }
    public void loop(){
        drive();
        intake();
        launch();
        arm();
        //lift();
    }

    public void drive() {
        double y = -gamepad1.left_stick_y; // This is reversed
        double x = gamepad1.left_stick_x; // Counteract imperfect strafing
        double z = gamepad1.right_stick_x;

        double leftFrontPower = y + x + z;
        double leftRearPower = y - x + z;
        double rightFrontPower = y - x - z;
        double rightRearPower = y + x - z;

        if (Math.abs(leftFrontPower) > 1 || Math.abs(leftRearPower) > 1 ||
                Math.abs(rightFrontPower) > 1 || Math.abs(rightRearPower) > 1 ){
            // Find the largest power
            double max;
            max = Math.max(Math.abs(leftFrontPower), Math.abs(leftRearPower));
            max = Math.max(Math.abs(rightFrontPower), max);
            max = Math.max(Math.abs(rightRearPower), max);

            // Everything is Positive, do not worry about signs
            leftFrontPower /= max;
            leftRearPower /= max;
            rightFrontPower /= max;
            rightRearPower /= max;
        }

        if(gamepad1.dpad_left){
            leftFrontPower = -1;
            rightRearPower = 1;
            leftRearPower = 1;
            rightFrontPower = -1;
        }
        else if(gamepad1.dpad_right){
            leftFrontPower = 1;
            rightRearPower = -1;
            leftRearPower = -1;
            rightFrontPower = 1;
        }
        else if (gamepad1.dpad_up)
        {
           leftFrontPower = 1;
           rightRearPower = 1;
           leftRearPower = 1;
           rightFrontPower = 1;
        }
        else if(gamepad1.dpad_down)
        {
            leftFrontPower = -1;
            leftRearPower = -1;
            rightRearPower = -1;
            rightFrontPower = -1;
        }

        if(gamepad1.square && currentMode == FAST_MODE && buttonTime.time() >= 500) {
            currentMode = PREC_MODE;
            buttonTime.reset();
        }
        else if(gamepad1.square && currentMode == PREC_MODE && buttonTime.time() >= 500) {
            currentMode = FAST_MODE;
            buttonTime.reset();
        }

        hardware.frontLeft.setPower(leftFrontPower * currentMode);
        hardware.rearLeft.setPower(leftRearPower * currentMode);
        hardware.frontRight.setPower(rightFrontPower * currentMode);
        hardware.rearRight.setPower(rightRearPower * currentMode);
    }

    public void intake(){
        double intakeOn = -1;
        double intakeOff = 0.0;
        
        if(gamepad2.square){
            hardware.intakeMotor.setPower(intakeOn);
        }
        else{
            hardware.intakeMotor.setPower(intakeOff);
        }
    }

    public void launch(){
        double flyWheelOn = 1;
        double flyWheelOff = 0.0;
        boolean pushServo;

        if(gamepad2.left_trigger>0){
            hardware.flyWheelMotor.setPower(flyWheelOn);
        }
        else{
            hardware.flyWheelMotor.setPower(flyWheelOff);
        }
        if(gamepad2.right_trigger>0){
            pushServo = true;
        }
        else{
            pushServo = false;
        }
        if(pushServo){
            hardware.pushServo.setPosition(1.0);
        }
        else{
            hardware.pushServo.setPosition(0.5);
        }

    }

//    public void lift(){
//        double y = gamepad2.left_stick_y *.1;
//        hardware.liftMotor.setPower(y);
//    }
    public void arm(){
        double on = .25;
        double hold = .17;
        double off = 0.0;
        if(gamepad1.square){
            hardware.armMotor.setPower(on);
        }
        else if(gamepad1.circle){
            hardware.armMotor.setPower(-1*on);
        }
        else if(gamepad1.cross)
            hardware.armMotor.setPower(hold);
        else{
            hardware.armMotor.setPower(off);
        }
    }
}