package com.westlake.air.pecs.rtnormalizer;

import com.westlake.air.pecs.domain.bean.RtIntensityPairs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nico Wang Ruimin
 * Time: 2018-07-31 19-16
 */

//get picked chromatogram
public class PeakPicker {

    private float signalToNoiseLimit = 1.0f;
    private int missingLimit = 1;
    private float threshold = 0.000001f;

    public List<float[]> pickMaxPeak(RtIntensityPairs rtIntensityPairs, float[] signalToNoise){
        if(rtIntensityPairs.getRtArray().length < 5) {
            return null;
        }
        List<float[]> maxPeaks = new ArrayList<>();
        float[] peak = new float[2];
        float centralPeakRt, leftNeighborRt, rightNeighborRt;
        float centralPeakInt, leftBoundaryInt, rightBoundaryInt;
        float stnLeft, stnMiddle, stnRight;
        int leftBoundary, rightBoundary;
        int missing;
        float maxPeakRt, maxPeakInt;
        float leftHand, rightHand;
        float mid, midDerivVal;


        for(int i = 2; i<rtIntensityPairs.getRtArray().length - 2; i++){
            leftNeighborRt = rtIntensityPairs.getRtArray()[i - 1];
            centralPeakRt = rtIntensityPairs.getRtArray()[i];
            rightNeighborRt = rtIntensityPairs.getRtArray()[i + 1];

            leftBoundaryInt = rtIntensityPairs.getIntensityArray()[i - 1];
            centralPeakInt = rtIntensityPairs.getIntensityArray()[i];
            rightBoundaryInt = rtIntensityPairs.getIntensityArray()[i + 1];

            if(Math.abs(centralPeakInt - rightBoundaryInt) < 0.0001) continue;
            if(Math.abs(centralPeakInt - leftBoundaryInt) < 0.0001) continue;

            stnLeft = signalToNoise[i-1];
            stnMiddle = signalToNoise[i];
            stnRight = signalToNoise[i+1];

            if(centralPeakInt > leftBoundaryInt &&
                    centralPeakInt > rightBoundaryInt &&
                    stnLeft >= signalToNoiseLimit &&
                    stnMiddle >= signalToNoiseLimit &&
                    stnRight >= signalToNoiseLimit){

                // find left boundary
                missing = 0;
                leftBoundary = i - 1;
                for(int left = 2; left < i+1; left++){

                    stnLeft = signalToNoise[i-left];

                    //zeroLeft
                    if(rtIntensityPairs.getIntensityArray()[i - left] == 0){
                        break;
                    }

                    if(rtIntensityPairs.getIntensityArray()[i - left] < leftBoundaryInt){
                        if(stnLeft >= signalToNoiseLimit){
                            leftBoundaryInt = rtIntensityPairs.getIntensityArray()[i - left];
                            leftBoundary = i - left;
                        }else {
                            missing ++;
                            if(missing <= missingLimit){
                                leftBoundaryInt = rtIntensityPairs.getIntensityArray()[i - left];
                                leftBoundary = i - left;
                            }else {
                                break;
                            }
                        }
                    }else {
                        break;
                    }
                }

                // find right boundary
                missing = 0;
                rightBoundary = i + 1;
                for(int right = 2; right < rtIntensityPairs.getRtArray().length - i; right++){

                    stnRight = signalToNoise[i+right];

                    //zeroLeft
                    if(rtIntensityPairs.getIntensityArray()[i + right] == 0){
                        break;
                    }

                    if(rtIntensityPairs.getIntensityArray()[i + right] < rightBoundaryInt){
                        if(stnRight >= signalToNoiseLimit){
                            rightBoundaryInt = rtIntensityPairs.getIntensityArray()[i + right];
                            rightBoundary = i + right;
                        }else {
                            missing ++;
                            if(missing <= missingLimit){
                                rightBoundaryInt = rtIntensityPairs.getIntensityArray()[i + right];
                                rightBoundary = i + right;
                            }else {
                                break;
                            }
                        }
                    }else {
                        break;
                    }
                }

                PeakSpline peakSpline = new PeakSpline();
                peakSpline.init(rtIntensityPairs, leftBoundary, rightBoundary);
                maxPeakRt =  centralPeakRt;
                maxPeakInt = centralPeakInt;
                leftHand = leftNeighborRt;
                rightHand = rightNeighborRt;

                while (rightHand - leftHand > threshold){
                    mid = (leftHand + rightHand) / 2.0f;
                    midDerivVal = peakSpline.derivatives(mid);
                    if(Math.abs(midDerivVal) < 0.0001){
                        break;
                    }
                    if(midDerivVal < 0.0f){
                        rightHand = mid;
                    }else {
                        leftHand = mid;
                    }
                }

                maxPeakRt = (leftHand + rightHand) /2.0f;
                maxPeakInt = peakSpline.eval(maxPeakRt);

                peak[0] = maxPeakRt;
                peak[1] = maxPeakInt;
                maxPeaks.add(peak);
                i = rightBoundary;
            }
        }
        return maxPeaks;
    }

}