package com.lightcrafts.image.color;

public class BlackBody {
    public static void main(String args[]) {
        float t0 = 4000;
        for (int i = 0; i <= 10; i++) {
            float t = t0 + 1000*i;
            float xy[] = xy(t);
            float dxy[] = D(t);
            System.out.println("BB(" + t + ") = " + xy[0] + ":" + xy[1] + " -> " + t(xy[0]));
            System.out.println("D(" + t + ") = " + dxy[0] + ":" + dxy[1]);
        }
    }

    // Return x,y coordinates of the CIE D illuminant specified by t, good between 4000K and 25000K
    static float[] D(float t) {
        double x;
        if (t <= 7000)
            x = -4.6070E9 * Math.pow(t, -3.) + 2.9678E6 * Math.pow(t, -2.) + 0.09911E3 * Math.pow(t, -1.) + 0.244063;
        else
            x = -2.0064E9 * Math.pow(t, -3.) + 1.9018E6 * Math.pow(t, -2.) + 0.24748E3 * Math.pow(t, -1.) + 0.237040;
        double y = -3.000 * Math.pow(x, 2.) + 2.870 * x - 0.275;

        return new float[]{(float) x, (float) y};
    }

    public static float[] xy(float t) {
        if (t >= tMin) {
            int idx = Math.min((int) ((t - tMin) / tStep), PT10Deg.length-1);

            float entry[] = PT10Deg[idx];

            if (t <= tMax && entry[0] < t) {
                float next[] = PT10Deg[idx+1];

                float fraction = (t - entry[0])/ tStep;
                float x = entry[1] + (next[1] - entry[1]) * fraction;
                float y = entry[2] + (next[2] - entry[2]) * fraction;

                return new float[]{x, y};
            } else
                return new float[]{entry[1], entry[2]};
        } else
            return new float[]{PT10Deg[0][1], PT10Deg[0][2]};
    }

    public static float t(float x) {
        int idx = binarySearch(x);
        float val = PT10Deg[idx][1];

        if (val < x) {
            float xprev = PT10Deg[idx-1][1];
            float fraction = (x - xprev) / (xprev - val);
            return PT10Deg[idx-1][0] + (PT10Deg[idx-1][0] - PT10Deg[idx][0]) * fraction;
        } else
            return PT10Deg[idx][0];
    }

    private static int binarySearch(float key) {
        int low = 0;
        int high = PT10Deg.length-1;
        int mid = 0;
        while (low <= high) {
	    mid = (low + high) >> 1;
	    float midVal = PT10Deg[mid][1];

            int cmp;
            if (midVal < key) {
                cmp = -1;   // Neither val is NaN, thisVal is smaller
            } else if (midVal > key) {
                cmp = 1;    // Neither val is NaN, thisVal is larger
            } else {
                int midBits = Float.floatToIntBits(midVal);
                int keyBits = Float.floatToIntBits(key);
                cmp = (midBits == keyBits ?  0 : // Values are equal
                       (midBits < keyBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
                        1));                     // (0.0, -0.0) or (NaN, !NaN)
            }

	    if (cmp > 0)
		low = mid + 1;
	    else if (cmp < 0)
		high = mid - 1;
	    else
		return mid; // key found
	}
	return mid;  // best approx.
    }

    static final int tStep = 100;
    static final int tMin = 1000;
    static final int tMax = 40000;

    static final float PT10Deg[][] = {
        {1000, 0.6472f, 0.3506f},
        {1100, 0.6348f, 0.3612f},
        {1200, 0.6225f, 0.3710f},
        {1300, 0.6104f, 0.3797f},
        {1400, 0.5983f, 0.3874f},
        {1500, 0.5864f, 0.3940f},
        {1600, 0.5747f, 0.3996f},
        {1700, 0.5632f, 0.4041f},
        {1800, 0.5519f, 0.4077f},
        {1900, 0.5408f, 0.4104f},
        {2000, 0.5300f, 0.4123f},
        {2100, 0.5195f, 0.4134f},
        {2200, 0.5093f, 0.4139f},
        {2300, 0.4995f, 0.4138f},
        {2400, 0.4900f, 0.4132f},
        {2500, 0.4808f, 0.4122f},
        {2600, 0.4720f, 0.4108f},
        {2700, 0.4636f, 0.4091f},
        {2800, 0.4555f, 0.4071f},
        {2900, 0.4478f, 0.4050f},
        {3000, 0.4404f, 0.4026f},
        {3100, 0.4333f, 0.4002f},
        {3200, 0.4266f, 0.3976f},
        {3300, 0.4202f, 0.3950f},
        {3400, 0.4140f, 0.3923f},
        {3500, 0.4082f, 0.3895f},
        {3600, 0.4026f, 0.3868f},
        {3700, 0.3973f, 0.3841f},
        {3800, 0.3922f, 0.3813f},
        {3900, 0.3873f, 0.3786f},
        {4000, 0.3827f, 0.3759f},
        {4100, 0.3783f, 0.3733f},
        {4200, 0.3741f, 0.3707f},
        {4300, 0.3701f, 0.3681f},
        {4400, 0.3662f, 0.3656f},
        {4500, 0.3625f, 0.3631f},
        {4600, 0.3590f, 0.3607f},
        {4700, 0.3557f, 0.3583f},
        {4800, 0.3524f, 0.3560f},
        {4900, 0.3494f, 0.3538f},
        {5000, 0.3464f, 0.3516f},
        {5100, 0.3436f, 0.3494f},
        {5200, 0.3409f, 0.3473f},
        {5300, 0.3383f, 0.3453f},
        {5400, 0.3358f, 0.3433f},
        {5500, 0.3334f, 0.3413f},
        {5600, 0.3311f, 0.3394f},
        {5700, 0.3289f, 0.3376f},
        {5800, 0.3268f, 0.3358f},
        {5900, 0.3247f, 0.3341f},
        {6000, 0.3228f, 0.3324f},
        {6100, 0.3209f, 0.3307f},
        {6200, 0.3190f, 0.3291f},
        {6300, 0.3173f, 0.3275f},
        {6400, 0.3156f, 0.3260f},
        {6500, 0.3140f, 0.3245f},
        {6600, 0.3124f, 0.3231f},
        {6700, 0.3109f, 0.3217f},
        {6800, 0.3094f, 0.3203f},
        {6900, 0.3080f, 0.3190f},
        {7000, 0.3066f, 0.3177f},
        {7100, 0.3053f, 0.3164f},
        {7200, 0.3040f, 0.3152f},
        {7300, 0.3028f, 0.3140f},
        {7400, 0.3016f, 0.3128f},
        {7500, 0.3004f, 0.3117f},
        {7600, 0.2993f, 0.3106f},
        {7700, 0.2982f, 0.3095f},
        {7800, 0.2971f, 0.3084f},
        {7900, 0.2961f, 0.3074f},
        {8000, 0.2951f, 0.3064f},
        {8100, 0.2942f, 0.3054f},
        {8200, 0.2932f, 0.3044f},
        {8300, 0.2923f, 0.3035f},
        {8400, 0.2914f, 0.3026f},
        {8500, 0.2906f, 0.3017f},
        {8600, 0.2897f, 0.3008f},
        {8700, 0.2889f, 0.3000f},
        {8800, 0.2881f, 0.2992f},
        {8900, 0.2874f, 0.2983f},
        {9000, 0.2866f, 0.2976f},
        {9100, 0.2859f, 0.2968f},
        {9200, 0.2852f, 0.2960f},
        {9300, 0.2845f, 0.2953f},
        {9400, 0.2839f, 0.2946f},
        {9500, 0.2832f, 0.2939f},
        {9600, 0.2826f, 0.2932f},
        {9700, 0.2820f, 0.2925f},
        {9800, 0.2813f, 0.2918f},
        {9900, 0.2808f, 0.2912f},
        {10000, 0.2802f, 0.2905f},
        {10100, 0.2796f, 0.2899f},
        {10200, 0.2791f, 0.2893f},
        {10300, 0.2785f, 0.2887f},
        {10400, 0.2780f, 0.2881f},
        {10500, 0.2775f, 0.2876f},
        {10600, 0.2770f, 0.2870f},
        {10700, 0.2765f, 0.2865f},
        {10800, 0.2761f, 0.2859f},
        {10900, 0.2756f, 0.2854f},
        {11000, 0.2751f, 0.2849f},
        {11100, 0.2747f, 0.2844f},
        {11200, 0.2743f, 0.2839f},
        {11300, 0.2738f, 0.2834f},
        {11400, 0.2734f, 0.2829f},
        {11500, 0.2730f, 0.2825f},
        {11600, 0.2726f, 0.2820f},
        {11700, 0.2722f, 0.2816f},
        {11800, 0.2719f, 0.2811f},
        {11900, 0.2715f, 0.2807f},
        {12000, 0.2711f, 0.2803f},
        {12100, 0.2708f, 0.2799f},
        {12200, 0.2704f, 0.2794f},
        {12300, 0.2701f, 0.2790f},
        {12400, 0.2697f, 0.2786f},
        {12500, 0.2694f, 0.2783f},
        {12600, 0.2691f, 0.2779f},
        {12700, 0.2688f, 0.2775f},
        {12800, 0.2684f, 0.2771f},
        {12900, 0.2681f, 0.2768f},
        {13000, 0.2678f, 0.2764f},
        {13100, 0.2675f, 0.2761f},
        {13200, 0.2673f, 0.2757f},
        {13300, 0.2670f, 0.2754f},
        {13400, 0.2667f, 0.2751f},
        {13500, 0.2664f, 0.2747f},
        {13600, 0.2662f, 0.2744f},
        {13700, 0.2659f, 0.2741f},
        {13800, 0.2656f, 0.2738f},
        {13900, 0.2654f, 0.2735f},
        {14000, 0.2651f, 0.2732f},
        {14100, 0.2649f, 0.2729f},
        {14200, 0.2646f, 0.2726f},
        {14300, 0.2644f, 0.2723f},
        {14400, 0.2642f, 0.2720f},
        {14500, 0.2639f, 0.2718f},
        {14600, 0.2637f, 0.2715f},
        {14700, 0.2635f, 0.2712f},
        {14800, 0.2633f, 0.2709f},
        {14900, 0.2631f, 0.2707f},
        {15000, 0.2629f, 0.2704f},
        {15100, 0.2626f, 0.2702f},
        {15200, 0.2624f, 0.2699f},
        {15300, 0.2622f, 0.2697f},
        {15400, 0.2620f, 0.2694f},
        {15500, 0.2618f, 0.2692f},
        {15600, 0.2617f, 0.2690f},
        {15700, 0.2615f, 0.2687f},
        {15800, 0.2613f, 0.2685f},
        {15900, 0.2611f, 0.2683f},
        {16000, 0.2609f, 0.2681f},
        {16100, 0.2607f, 0.2678f},
        {16200, 0.2606f, 0.2676f},
        {16300, 0.2604f, 0.2674f},
        {16400, 0.2602f, 0.2672f},
        {16500, 0.2601f, 0.2670f},
        {16600, 0.2599f, 0.2668f},
        {16700, 0.2597f, 0.2666f},
        {16800, 0.2596f, 0.2664f},
        {16900, 0.2594f, 0.2662f},
        {17000, 0.2593f, 0.2660f},
        {17100, 0.2591f, 0.2658f},
        {17200, 0.2590f, 0.2656f},
        {17300, 0.2588f, 0.2654f},
        {17400, 0.2587f, 0.2653f},
        {17500, 0.2585f, 0.2651f},
        {17600, 0.2584f, 0.2649f},
        {17700, 0.2582f, 0.2647f},
        {17800, 0.2581f, 0.2645f},
        {17900, 0.2580f, 0.2644f},
        {18000, 0.2578f, 0.2642f},
        {18100, 0.2577f, 0.2640f},
        {18200, 0.2575f, 0.2639f},
        {18300, 0.2574f, 0.2637f},
        {18400, 0.2573f, 0.2635f},
        {18500, 0.2572f, 0.2634f},
        {18600, 0.2570f, 0.2632f},
        {18700, 0.2569f, 0.2631f},
        {18800, 0.2568f, 0.2629f},
        {18900, 0.2567f, 0.2628f},
        {19000, 0.2566f, 0.2626f},
        {19100, 0.2564f, 0.2625f},
        {19200, 0.2563f, 0.2623f},
        {19300, 0.2562f, 0.2622f},
        {19400, 0.2561f, 0.2620f},
        {19500, 0.2560f, 0.2619f},
        {19600, 0.2559f, 0.2618f},
        {19700, 0.2558f, 0.2616f},
        {19800, 0.2557f, 0.2615f},
        {19900, 0.2555f, 0.2613f},
        {20000, 0.2554f, 0.2612f},
        {20100, 0.2553f, 0.2611f},
        {20200, 0.2552f, 0.2609f},
        {20300, 0.2551f, 0.2608f},
        {20400, 0.2550f, 0.2607f},
        {20500, 0.2549f, 0.2606f},
        {20600, 0.2548f, 0.2604f},
        {20700, 0.2547f, 0.2603f},
        {20800, 0.2546f, 0.2602f},
        {20900, 0.2546f, 0.2601f},
        {21000, 0.2545f, 0.2600f},
        {21100, 0.2544f, 0.2598f},
        {21200, 0.2543f, 0.2597f},
        {21300, 0.2542f, 0.2596f},
        {21400, 0.2541f, 0.2595f},
        {21500, 0.2540f, 0.2594f},
        {21600, 0.2539f, 0.2593f},
        {21700, 0.2538f, 0.2591f},
        {21800, 0.2537f, 0.2590f},
        {21900, 0.2537f, 0.2589f},
        {22000, 0.2536f, 0.2588f},
        {22100, 0.2535f, 0.2587f},
        {22200, 0.2534f, 0.2586f},
        {22300, 0.2533f, 0.2585f},
        {22400, 0.2533f, 0.2584f},
        {22500, 0.2532f, 0.2583f},
        {22600, 0.2531f, 0.2582f},
        {22700, 0.2530f, 0.2581f},
        {22800, 0.2529f, 0.2580f},
        {22900, 0.2529f, 0.2579f},
        {23000, 0.2528f, 0.2578f},
        {23100, 0.2527f, 0.2577f},
        {23200, 0.2526f, 0.2576f},
        {23300, 0.2526f, 0.2575f},
        {23400, 0.2525f, 0.2574f},
        {23500, 0.2524f, 0.2573f},
        {23600, 0.2524f, 0.2572f},
        {23700, 0.2523f, 0.2572f},
        {23800, 0.2522f, 0.2571f},
        {23900, 0.2521f, 0.2570f},
        {24000, 0.2521f, 0.2569f},
        {24100, 0.2520f, 0.2568f},
        {24200, 0.2519f, 0.2567f},
        {24300, 0.2519f, 0.2566f},
        {24400, 0.2518f, 0.2565f},
        {24500, 0.2517f, 0.2565f},
        {24600, 0.2517f, 0.2564f},
        {24700, 0.2516f, 0.2563f},
        {24800, 0.2516f, 0.2562f},
        {24900, 0.2515f, 0.2561f},
        {25000, 0.2514f, 0.2560f},
        {25100, 0.2514f, 0.2560f},
        {25200, 0.2513f, 0.2559f},
        {25300, 0.2512f, 0.2558f},
        {25400, 0.2512f, 0.2557f},
        {25500, 0.2511f, 0.2557f},
        {25600, 0.2511f, 0.2556f},
        {25700, 0.2510f, 0.2555f},
        {25800, 0.2510f, 0.2554f},
        {25900, 0.2509f, 0.2554f},
        {26000, 0.2508f, 0.2553f},
        {26100, 0.2508f, 0.2552f},
        {26200, 0.2507f, 0.2551f},
        {26300, 0.2507f, 0.2551f},
        {26400, 0.2506f, 0.2550f},
        {26500, 0.2506f, 0.2549f},
        {26600, 0.2505f, 0.2548f},
        {26700, 0.2505f, 0.2548f},
        {26800, 0.2504f, 0.2547f},
        {26900, 0.2503f, 0.2546f},
        {27000, 0.2503f, 0.2546f},
        {27100, 0.2502f, 0.2545f},
        {27200, 0.2502f, 0.2544f},
        {27300, 0.2501f, 0.2544f},
        {27400, 0.2501f, 0.2543f},
        {27500, 0.2500f, 0.2542f},
        {27600, 0.2500f, 0.2542f},
        {27700, 0.2499f, 0.2541f},
        {27800, 0.2499f, 0.2541f},
        {27900, 0.2499f, 0.2540f},
        {28000, 0.2498f, 0.2539f},
        {28100, 0.2498f, 0.2539f},
        {28200, 0.2497f, 0.2538f},
        {28300, 0.2497f, 0.2537f},
        {28400, 0.2496f, 0.2537f},
        {28500, 0.2496f, 0.2536f},
        {28600, 0.2495f, 0.2536f},
        {28700, 0.2495f, 0.2535f},
        {28800, 0.2494f, 0.2534f},
        {28900, 0.2494f, 0.2534f},
        {29000, 0.2493f, 0.2533f},
        {29100, 0.2493f, 0.2533f},
        {29200, 0.2493f, 0.2532f},
        {29300, 0.2492f, 0.2532f},
        {29400, 0.2492f, 0.2531f},
        {29500, 0.2491f, 0.2530f},
        {29600, 0.2491f, 0.2530f},
        {29700, 0.2490f, 0.2529f},
        {29800, 0.2490f, 0.2529f},
        {29900, 0.2490f, 0.2528f},
        {30000, 0.2489f, 0.2528f},
        {30100, 0.2489f, 0.2527f},
        {30200, 0.2488f, 0.2527f},
        {30300, 0.2488f, 0.2526f},
        {30400, 0.2488f, 0.2526f},
        {30500, 0.2487f, 0.2525f},
        {30600, 0.2487f, 0.2525f},
        {30700, 0.2487f, 0.2524f},
        {30800, 0.2486f, 0.2524f},
        {30900, 0.2486f, 0.2523f},
        {31000, 0.2485f, 0.2523f},
        {31100, 0.2485f, 0.2522f},
        {31200, 0.2485f, 0.2522f},
        {31300, 0.2484f, 0.2521f},
        {31400, 0.2484f, 0.2521f},
        {31500, 0.2484f, 0.2520f},
        {31600, 0.2483f, 0.2520f},
        {31700, 0.2483f, 0.2519f},
        {31800, 0.2482f, 0.2519f},
        {31900, 0.2482f, 0.2518f},
        {32000, 0.2482f, 0.2518f},
        {32100, 0.2481f, 0.2517f},
        {32200, 0.2481f, 0.2517f},
        {32300, 0.2481f, 0.2516f},
        {32400, 0.2480f, 0.2516f},
        {32500, 0.2480f, 0.2516f},
        {32600, 0.2480f, 0.2515f},
        {32700, 0.2479f, 0.2515f},
        {32800, 0.2479f, 0.2514f},
        {32900, 0.2479f, 0.2514f},
        {33000, 0.2478f, 0.2513f},
        {33100, 0.2478f, 0.2513f},
        {33200, 0.2478f, 0.2513f},
        {33300, 0.2477f, 0.2512f},
        {33400, 0.2477f, 0.2512f},
        {33500, 0.2477f, 0.2511f},
        {33600, 0.2476f, 0.2511f},
        {33700, 0.2476f, 0.2510f},
        {33800, 0.2476f, 0.2510f},
        {33900, 0.2476f, 0.2510f},
        {34000, 0.2475f, 0.2509f},
        {34100, 0.2475f, 0.2509f},
        {34200, 0.2475f, 0.2508f},
        {34300, 0.2474f, 0.2508f},
        {34400, 0.2474f, 0.2508f},
        {34500, 0.2474f, 0.2507f},
        {34600, 0.2473f, 0.2507f},
        {34700, 0.2473f, 0.2506f},
        {34800, 0.2473f, 0.2506f},
        {34900, 0.2473f, 0.2506f},
        {35000, 0.2472f, 0.2505f},
        {35100, 0.2472f, 0.2505f},
        {35200, 0.2472f, 0.2505f},
        {35300, 0.2471f, 0.2504f},
        {35400, 0.2471f, 0.2504f},
        {35500, 0.2471f, 0.2503f},
        {35600, 0.2471f, 0.2503f},
        {35700, 0.2470f, 0.2503f},
        {35800, 0.2470f, 0.2502f},
        {35900, 0.2470f, 0.2502f},
        {36000, 0.2470f, 0.2502f},
        {36100, 0.2469f, 0.2501f},
        {36200, 0.2469f, 0.2501f},
        {36300, 0.2469f, 0.2501f},
        {36400, 0.2468f, 0.2500f},
        {36500, 0.2468f, 0.2500f},
        {36600, 0.2468f, 0.2500f},
        {36700, 0.2468f, 0.2499f},
        {36800, 0.2467f, 0.2499f},
        {36900, 0.2467f, 0.2498f},
        {37000, 0.2467f, 0.2498f},
        {37100, 0.2467f, 0.2498f},
        {37200, 0.2466f, 0.2497f},
        {37300, 0.2466f, 0.2497f},
        {37400, 0.2466f, 0.2497f},
        {37500, 0.2466f, 0.2497f},
        {37600, 0.2465f, 0.2496f},
        {37700, 0.2465f, 0.2496f},
        {37800, 0.2465f, 0.2496f},
        {37900, 0.2465f, 0.2495f},
        {38000, 0.2465f, 0.2495f},
        {38100, 0.2464f, 0.2495f},
        {38200, 0.2464f, 0.2494f},
        {38300, 0.2464f, 0.2494f},
        {38400, 0.2464f, 0.2494f},
        {38500, 0.2463f, 0.2493f},
        {38600, 0.2463f, 0.2493f},
        {38700, 0.2463f, 0.2493f},
        {38800, 0.2463f, 0.2492f},
        {38900, 0.2462f, 0.2492f},
        {39000, 0.2462f, 0.2492f},
        {39100, 0.2462f, 0.2492f},
        {39200, 0.2462f, 0.2491f},
        {39300, 0.2462f, 0.2491f},
        {39400, 0.2461f, 0.2491f},
        {39500, 0.2461f, 0.2490f},
        {39600, 0.2461f, 0.2490f},
        {39700, 0.2461f, 0.2490f},
        {39800, 0.2460f, 0.2490f},
        {39900, 0.2460f, 0.2489f},
        {40000, 0.2460f, 0.2489f}
    };
}
