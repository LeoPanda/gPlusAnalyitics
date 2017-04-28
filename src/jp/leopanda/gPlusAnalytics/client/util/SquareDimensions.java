package jp.leopanda.gPlusAnalytics.client.util;

/**
 * 矩形の縦横寸法
 * @author LeoPanda
 *
 */
public class SquareDimensions {
  private double width;
  private double height;
  public SquareDimensions(double width,double height){
    this.width = width;
    this.height = height;
  }
  public double getWidth(){
    return this.width;
  }
  public double getHeight(){
    return this.height;
  }

}
