package com.sszg.apprenticechef;

import java.util.ArrayList;

public class RecipeMath {
    public static void main(String[] args) {
        ArrayList<String> input = new ArrayList<String>();
        input.add("2.4 cups sugar");
        input.add("5 large eggs");
        input.add("2 2/3 cups milk");
        input.add("2 teaspoons pure vanilla extract");
        input.add("3 cups cubed Italian bread");
        input.add("1/2 cup packed light brown sugar");
        input.add("A pinch of salt");
        //Input done
        System.out.println(GetBestMeasure(input, 9, true));
    }

    public static ArrayList<String> GetBestMeasure(ArrayList<String> input, double divisor, Boolean giveFraction) {
        ArrayList<String> output = new ArrayList<>();
        String[] VolumeTypes = {"gal", "L", "qt", "pt", "C", "fl oz", "Tbsp", "tsp"};
        String[] WeightTypes = {"kg", "lbs", "oz", "g"};
        double[][] volumeConversions = {{1, 3.84, 4, 8, 16, 128, 256, 768}, {0.260417, 1, 1.04166, 2.08332, 4.125, 33, 66, 200}, {0.25, 0.96, 1, 2, 4, 32, 64, 192},
                {0.125, 0.48, 0.5, 1, 2, 16, 32, 96}, {0.0625, 0.24, 0.25, 0.5, 1, 8, 16, 48}, {0.0078125, 0.03, 0.03125, 0.0625, 0.125, 1, 2, 6},
                {0.00390625, 0.015, 0.015625, 0.03125, 0.0625, 0.5, 1, 3}, {0.001302083, 0.005, 0.00520833, 0.0104166, 0.020625, 0.165, 0.33, 1},
                {0.000260417, 0.001, 0.00104166, 0.00208332, 0.004125, 0.033, 0.066, 0.2}};
        double[][] weightConversions = {{1, 2.20462, 35.274, 1000}, {0.453592, 1, 16, 453.592}, {0.0283495, 0.0625, 1, 28.3495},
                {0.001, 0.00220462, 0.035274, 1}};
        Double curAmount = 0.0;
        Double temp;
        Boolean calc = true;
        Boolean twoNum = false;
        String[] inputArray;
        String type = "";
        for (int h = 0; h < input.size(); h++) {
            inputArray = (input.get(h)).split(" ", 4);
            if (inputArray[0].contains("/")) {
                String[] GetParts = inputArray[0].split("/");
                curAmount = Double.parseDouble(GetParts[0]) / Double.parseDouble(GetParts[1]);
            } else if (inputArray[0].matches(".*\\d+.*")) {
                curAmount = Double.parseDouble(inputArray[0]);
                if (inputArray[1].contains("/")) {
                    twoNum = true;
                    String[] GetParts = inputArray[1].split("/");
                    curAmount += Double.parseDouble(GetParts[0]) / Double.parseDouble(GetParts[1]);
                }
            } else {
                calc = false;
                output.add(input.get(h));
            }
            if (calc) {
                if (twoNum) {
                    type = inputArray[2];
                } else {
                    type = inputArray[1];
                }
                int numType = -1;
                double[] convertedVolume = new double[8];
                double[] convertedWeight = new double[4];
                int smallest = 0;
                int fluid = 0;

                if (type.contains("gallon") || type.contains("Gallon")) {
                    numType = 0;
                } else if (type.contains("liter") || type.contains("Liter")) {
                    numType = 1;
                } else if (type.contains("quart") || type.contains("Quart")) {
                    numType = 2;
                } else if (type.contains("pint") || type.contains("Pint")) {
                    numType = 3;
                } else if (type.contains("cup") || type.contains("Cup")) {
                    numType = 4;
                } else if (type.contains("fluid ounce") || type.contains("Fluid ounce") || type.contains("Fluid Ounce")) {
                    numType = 5;
                } else if (type.contains("tablespoon") || type.contains("Tablespoon") || type.contains("Tbsp")) {
                    numType = 6;
                } else if (type.contains("teaspoon") || type.contains("Teaspoon") || type.contains("tsp")) {
                    numType = 7;
                } else {
                    fluid = 1;
                }

                if (fluid == 1) {
                    if (type.contains("kilogram") || type.contains("Kilogram") || type.contains("kg") || type.contains("Kg")) {
                        numType = 0;
                    } else if (type.contains("pound") || type.contains("Pound") || type.contains("lb")) {
                        numType = 1;
                    } else if (type.contains("ounce") || type.contains("Ounce") || type.contains("oz") || type.contains("Oz")) {
                        numType = 2;
                    } else if (type.contains("gram") || type.contains("Gram")) {
                        numType = 3;
                    } else {
                        if ((curAmount / divisor) == Math.ceil(curAmount / divisor)) {
                            output.add((int) Math.floor(curAmount / divisor) + " " + inputArray[1] + " " + inputArray[2]);
                        } else if (!giveFraction) {
                            temp = Math.floor(100 * curAmount / divisor);
                            output.add((temp / 100 + " " + inputArray[1] + " " + inputArray[2]));
                        } else {
                            output.add(decimalToFraction(curAmount / divisor, curAmount, divisor, inputArray, 1, 1)
                                    + " " + inputArray[2]);
                        }
                        fluid = 2;
                    }
                }

                if (fluid == 0) {
                    for (int i = 0; i < 8; i++) {
                        convertedVolume[i] = curAmount * volumeConversions[numType][i] / divisor; //Array of converted amounts
                    }
                    for (int j = 7; j >= 0; j--) {
                        if (convertedVolume[j] >= 1) {
                            smallest = j;
                        }
                    }
                    if (smallest == 0 && convertedVolume[0] < 1) {
                        smallest = 7;
                    }
                    if (convertedVolume[smallest] == Math.ceil(convertedVolume[smallest])) {
                        output.add(((int) convertedVolume[smallest] + " " + VolumeTypes[smallest] + " " + inputArray[2]));
                    } else if (!giveFraction) {
                        temp = Math.floor(100 * convertedVolume[smallest]);
                        output.add((temp / 100 + " " + VolumeTypes[smallest] + " " + inputArray[2]));
                    } else {
                        output.add(decimalToFraction(convertedVolume[smallest], curAmount, divisor, VolumeTypes, smallest, numType) + " " + inputArray[2]);
                    }
                } else if (fluid == 1) {
                    for (int i = 0; i < 4; i++) {
                        convertedWeight[i] = curAmount * weightConversions[numType][i] / divisor; //Array of converted amounts
                    }
                    for (int j = 3; j >= 0; j--) {
                        if (convertedWeight[j] >= 1) {
                            smallest = j;
                        }
                    }
                    if (smallest == 0 && convertedWeight[0] < 1) {
                        smallest = 3;
                    }
                    if ((convertedWeight[smallest] != Math.ceil(convertedWeight[smallest])) && giveFraction) {
                        output.add(decimalToFraction(convertedWeight[smallest], curAmount, divisor, WeightTypes, smallest, numType) + " " + inputArray[2]);
                    } else if (!giveFraction) {
                        temp = Math.floor(100 * convertedWeight[smallest]);
                        output.add(temp / 100 + " " + WeightTypes[smallest] + " " + inputArray[2]);
                    } else {
                        output.add((int) Math.floor(convertedWeight[smallest]) + " " + WeightTypes[smallest] + " " + inputArray[2]);
                    }
                }
            }
        }
        return output;
    }

    public static String decimalToFraction(Double inDec, double curAmount, double divisor, String[] Types, int smallest, int numtype) {
        int numerator = 1;
        int counter = 0;
        double temp = Math.round(100 / (inDec - (int) Math.floor(inDec)));
        temp /= 100;
        while (temp != Math.ceil(temp)) {
            counter++;
            numerator *= 2;
            temp *= 2;
            if (counter == 7) {
                return (int) curAmount + "/" + (int) divisor + " " + Types[numtype];
            }
        }
        if ((int) Math.floor(inDec) != 0) {
            return (int) Math.floor(inDec) + " " + numerator + "/" + (int) temp + " " + Types[smallest];
        } else {
            return numerator + "/" + (int) temp + " " + Types[smallest];
        }
    }
}
