package screens;

import com.badlogic.gdx.utils.ObjectMap;

import engine.GridData;

public class GridTools {

    public static int[] holes = new int[]{9,32,33,40};

    private static final short NORTH = 2;
    private static final short EAST = 16;
    private static final short SOUTH = 64;
    private static final short WEST = 8;
    private static final short NORTH_WEST = 1;
    private static final short NORTH_EAST = 4;
    private static final short SOUTH_EAST = 128;
    private static final short SOUTH_WEST = 32;

    private static ObjectMap<Integer, Integer> pick_tile = new ObjectMap<Integer,Integer>();
    static {
//
        pick_tile.put(	0	, 47	);
        pick_tile.put(	1	, 35	);
        pick_tile.put(	2	, 47	);
        pick_tile.put(	3	, 19	);
        pick_tile.put(	4	, 7	);
        pick_tile.put(	5	, 36	);
        pick_tile.put(	6	, 19	);
        pick_tile.put(	7	, 19	);
        pick_tile.put(	8	, 26	);
        pick_tile.put(	9	, 35	);
        pick_tile.put(	10	, 47	);
        pick_tile.put(	11	, 35	);
        pick_tile.put(	12	, 7	);
        pick_tile.put(	13	, 26	);
        pick_tile.put(	14	, 18	);
        pick_tile.put(	15	, 18	);
        pick_tile.put(	16	, 24	);
        pick_tile.put(	17	, 24	);
        pick_tile.put(	18	, 3	);
        pick_tile.put(	19	, 16	);
        pick_tile.put(	20	, 24	);
        pick_tile.put(	21	, 24	);
        pick_tile.put(	22	, 21    );
        pick_tile.put(	23	, 16	);
        pick_tile.put(	24	, 25	);
        pick_tile.put(	25	, 25	);
        pick_tile.put(	26	, 22	);
        pick_tile.put(	27	, 43	);
        pick_tile.put(	28	, 25	);
        pick_tile.put(	29	, 25	);
        pick_tile.put(	30	, 44	);
        pick_tile.put(	31	, 17	);
        pick_tile.put(	32	, 47	);
        pick_tile.put(	33	, 35	);
        pick_tile.put(	34	, 47	);
        pick_tile.put(	35	, 19	);
        pick_tile.put(	36	, 7	);
        pick_tile.put(	37	, 36	);
        pick_tile.put(	38	, 19	);
        pick_tile.put(	39	, 19	);
        pick_tile.put(	40	, 26	);
        pick_tile.put(	41	, 35	);
        pick_tile.put(	42	, 23	);
        pick_tile.put(	43	, 35	);
        pick_tile.put(	44	, 7	);
        pick_tile.put(	45	, 36	);
        pick_tile.put(	46	, 7	);
        pick_tile.put(	47	, 18	);
        pick_tile.put(	48	, 24	);
        pick_tile.put(	49	, 24	);
        pick_tile.put(	50	, 3	);
        pick_tile.put(	51	, 16	);
        pick_tile.put(	52	, 4	);
        pick_tile.put(	53	, 24	);
        pick_tile.put(	54	, 4	);
        pick_tile.put(	55	, 16	);
        pick_tile.put(	56	, 25	);
        pick_tile.put(	57	, 25	);
        pick_tile.put(	58	, 3	);
        pick_tile.put(	59	, 43	);
        pick_tile.put(	60	, 25	);
        pick_tile.put(	61	, 25	);
        pick_tile.put(	62	, 44	);
        pick_tile.put(	63	, 17	);
        pick_tile.put(	64	, 3	);
        pick_tile.put(	65	, 38	);
        pick_tile.put(	66	, 11	);
        pick_tile.put(	67	, 11	);
        pick_tile.put(	68	, 8	);
        pick_tile.put(	69	, 37	);
        pick_tile.put(	70	, 11	);
        pick_tile.put(	71	, 11	);
        pick_tile.put(	72	, 7	);
        pick_tile.put(	73	, 7	);
        pick_tile.put(	74	, 15	    );
        pick_tile.put(	75	, 28	);
        pick_tile.put(	76	, 8	    );
        pick_tile.put(	77	, 7	);
        pick_tile.put(	78	, 35	);
        pick_tile.put(	79	, 35	);
        pick_tile.put(	80	, 5  	);
        pick_tile.put(	81	, 5	);
        pick_tile.put(	82	, 13	    );
        pick_tile.put(	83	, 36	);
        pick_tile.put(	84	, 5	    );
        pick_tile.put(	85	, 21	);
        pick_tile.put(	86	, 13	    );
        pick_tile.put(	87	, 36	);
        pick_tile.put(	88	, 6	);
        pick_tile.put(	89	, 6	);
        pick_tile.put(	90	, 14	);
        pick_tile.put(	91	, 39	);
        pick_tile.put(	92	, 6	);
        pick_tile.put(	93	, 6	);
        pick_tile.put(	94	, 37	);
        pick_tile.put(	95	, 31	);
        pick_tile.put(	96	, 3	);
        pick_tile.put(	97	, 38	);
        pick_tile.put(	98	, 11	);
        pick_tile.put(	99	, 11	);
        pick_tile.put(	100	, 8	);
        pick_tile.put(	101	, 37	);
        pick_tile.put(	102	, 11	);
        pick_tile.put(	103	, 11	);
        pick_tile.put(	104	, 7	);
        pick_tile.put(	105	, 7	);
        pick_tile.put(	106	, 15	);
        pick_tile.put(	107	, 15	);
        pick_tile.put(	108	, 8	);
        pick_tile.put(	109	, 7	);
        pick_tile.put(	110	, 35	);
        pick_tile.put(	111	, 35	);
        pick_tile.put(	112	, 0	);
        pick_tile.put(	113	, 0	);
        pick_tile.put(	114	, 42	);
        pick_tile.put(	115	, 8	);
        pick_tile.put(	116	, 0	);
        pick_tile.put(	117	, 0	);
        pick_tile.put(	118	, 42	);
        pick_tile.put(	119	, 8	);
        pick_tile.put(	120	, 38	);
        pick_tile.put(	121	, 38	);
        pick_tile.put(	122	, 47	);
        pick_tile.put(	123	, 28	);
        pick_tile.put(	124	, 38	);
        pick_tile.put(	125	, 38	);
        pick_tile.put(	126	, 4	);
        pick_tile.put(	127	, 33	);
        pick_tile.put(	128	, 47	);
        pick_tile.put(	129	, 35	);
        pick_tile.put(	130	, 47	);
        pick_tile.put(	131	, 35	);
        pick_tile.put(	132	, 7	);
        pick_tile.put(	133	, 27	);
        pick_tile.put(	134	, 19	);
        pick_tile.put(	135	, 19	);
        pick_tile.put(	136	, 26	);
        pick_tile.put(	137	, 35	);
        pick_tile.put(	138	, 23	);
        pick_tile.put(	139	, 35	);
        pick_tile.put(	140	, 26	);
        pick_tile.put(	141	, 26	);
        pick_tile.put(	142	, 18	);
        pick_tile.put(	143	, 18	);
        pick_tile.put(	144	, 3	);
        pick_tile.put(	145	, 19	);
        pick_tile.put(	146	, 3	);
        pick_tile.put(	147	, 16	);
        pick_tile.put(	148	, 4	);
        pick_tile.put(	149	, 24	);
        pick_tile.put(	150	, 4	);
        pick_tile.put(	151	, 16	);
        pick_tile.put(	152	, 25	);
        pick_tile.put(	153	, 25	);
        pick_tile.put(	154	, 22	);
        pick_tile.put(	155	, 43	);
        pick_tile.put(	156	, 25	);
        pick_tile.put(	157	, 25	);
        pick_tile.put(	158	, 44	);
        pick_tile.put(	159	, 17	);
        pick_tile.put(	160	, 27	);
        pick_tile.put(	161	, 35	);
        pick_tile.put(	162	, 47	);
        pick_tile.put(	163	, 35	);
        pick_tile.put(	164	, 7	);
        pick_tile.put(	165	, 27	);
        pick_tile.put(	166	, 19	);
        pick_tile.put(	167	, 19	);
        pick_tile.put(	168	, 26	);
        pick_tile.put(	169	, 35	);
        pick_tile.put(	170	, 23	);
        pick_tile.put(	171	, 23	);
        pick_tile.put(	172	, 26	);
        pick_tile.put(	173	, 26	);
        pick_tile.put(	174	, 18	);
        pick_tile.put(	175	, 18	);
        pick_tile.put(	176	, 24	);
        pick_tile.put(	177	, 24	);
        pick_tile.put(	178	, 22	);
        pick_tile.put(	179	, 16	);
        pick_tile.put(	180	, 4	);
        pick_tile.put(	181	, 24	);
        pick_tile.put(	182	, 21	);
        pick_tile.put(	183	, 16	);
        pick_tile.put(	184	, 25	);
        pick_tile.put(	185	, 25	);
        pick_tile.put(	186	, 22	);
        pick_tile.put(	187	, 43	);
        pick_tile.put(	188	, 25	);
        pick_tile.put(	189	, 25	);
        pick_tile.put(	190	, 44	);
        pick_tile.put(	191	, 17	);
        pick_tile.put(	192	, 3	);
        pick_tile.put(	193	, 34	);
        pick_tile.put(	194	, 11	);
        pick_tile.put(	195	, 11	);
        pick_tile.put(	196	, 3	);
        pick_tile.put(	197	, 46	);
        pick_tile.put(	198	, 11	);
        pick_tile.put(	199	, 11	);
        pick_tile.put(	200	, 2	    );
        pick_tile.put(	201	, 2	    );
        pick_tile.put(	202	, 45	);
        pick_tile.put(	203	, 45	);
        pick_tile.put(	204	, 2	);
        pick_tile.put(	205	, 2	);
        pick_tile.put(	206	, 10	);
        pick_tile.put(	207	, 10	);
        pick_tile.put(	208	, 5	    );
        pick_tile.put(	209	, 5	);
        pick_tile.put(	210	, 46	    );
        pick_tile.put(	211	, 36	);
        pick_tile.put(	212	, 5	    );
        pick_tile.put(	213	, 5	);
        pick_tile.put(	214	, 13	    );
        pick_tile.put(	215	, 36	);
        pick_tile.put(	216	, 34    );
        pick_tile.put(	217	, 34	);
        pick_tile.put(	218	, 46	);
        pick_tile.put(	219	, 12	);
        pick_tile.put(	220	, 34	);
        pick_tile.put(	221	, 34	);
        pick_tile.put(	222	, 30	);
        pick_tile.put(	223	, 32	);
        pick_tile.put(	224	, 3	    );
        pick_tile.put(	225	, 3	);
        pick_tile.put(	226	, 11	    );
        pick_tile.put(	227	, 11	);
        pick_tile.put(	228	, 3	    );
        pick_tile.put(	229	, 3	);
        pick_tile.put(	230	, 11	);
        pick_tile.put(	231	, 11	);
        pick_tile.put(	232	, 2	    );
        pick_tile.put(	233	, 2	    );
        pick_tile.put(	234	, 45	);
        pick_tile.put(	235	, 45	);
        pick_tile.put(	236	, 2	    );
        pick_tile.put(	237	, 2	    );
        pick_tile.put(	238	, 10	);
        pick_tile.put(	239	, 10	);
        pick_tile.put(	240	, 0	);
        pick_tile.put(	241	, 0	);
        pick_tile.put(	242	, 42	);
        pick_tile.put(	243	, 8	);
        pick_tile.put(	244	, 0	);
        pick_tile.put(	245	, 0	);
        pick_tile.put(	246	, 42	);
        pick_tile.put(	247	, 8	);
        pick_tile.put(	248	, 1	);
        pick_tile.put(	249	, 1	);
        pick_tile.put(	250	, 29	);
        pick_tile.put(	251	, 41	);
        pick_tile.put(	252	, 1	);
        pick_tile.put(	253	, 1	);
        pick_tile.put(	254	, 40	);
        pick_tile.put(	255	, 9	);

    }

    public static int getRowWidth(int y, GridData gridData){
        int count = 0;
        for(int col = 0; col < gridData.getCol(); col++){
            if(gridData.getDataValueAt(col,y) > 0) count++;
        }
        return count;
    }

    public static int getColHeight(int x, GridData gridData){
        int max = 0;
        for(int row = 0; row < gridData.getRow(); row++){
            if(gridData.getDataValueAt(x,row) > 0) max = Math.max(row,max);
        }
        return max;
    }

    public static int getAutoValue(final GridData gridData,int x, int y, byte value) {

//      // What tile transition we should have.
        int result = 0;
        boolean up =    gridData.safeGetDataValueAt(x,y+1) == (int)value;
        boolean right = gridData.safeGetDataValueAt(x-1,y) == (int)value;
        boolean down =  gridData.safeGetDataValueAt(x,y-1) == (int)value;
        boolean left =  gridData.safeGetDataValueAt(x+1,y) == (int)value;

        // If tile above, if tile down, etc.
        if(up)     result += NORTH;
        if(right)  result += EAST;
        if(down)   result += SOUTH;
        if(left)   result += WEST;
//
//      // This checks for diagonals
        boolean upLeft =     gridData.safeGetDataValueAt(x-1,y+1) == (int)value;
        boolean upRight =    gridData.safeGetDataValueAt(x+1,y+1) == (int)value;
        boolean downRight =  gridData.safeGetDataValueAt(x+1,y-1) == (int)value;
        boolean downLeft =   gridData.safeGetDataValueAt(x-1,y-1) == (int)value;

        if(upLeft)    result += NORTH_WEST;
        if(upRight)   result += NORTH_EAST;
        if(downRight) result += SOUTH_EAST;
        if(downLeft)  result += SOUTH_WEST;

        return pick_tile.get(result);
    }


    public static int getAutoValue(final GridData gridData,int x, int y) {

//      // What tile transition we should have.
        int result = 0;
        boolean up =    gridData.safeGetDataValueAt(x,y+1) >(byte)0;
        boolean right = gridData.safeGetDataValueAt(x-1,y) >(byte)0;
        boolean down =  gridData.safeGetDataValueAt(x,y-1) >(byte)0;
        boolean left =  gridData.safeGetDataValueAt(x+1,y) >(byte)0;

        // If tile above, if tile down, etc.
        if(up)     result += NORTH;
        if(right)  result += EAST;
        if(down)   result += SOUTH;
        if(left)   result += WEST;
//
//      // This checks for diagonals
        boolean upLeft =     gridData.safeGetDataValueAt(x-1,y+1) > (byte)0;
        boolean upRight =    gridData.safeGetDataValueAt(x+1,y+1) > (byte)0;
        boolean downRight =  gridData.safeGetDataValueAt(x+1,y-1) > (byte)0;
        boolean downLeft =   gridData.safeGetDataValueAt(x-1,y-1) > (byte)0;

        if(upLeft)    result += NORTH_WEST;
        if(upRight)   result += NORTH_EAST;
        if(downRight) result += SOUTH_EAST;
        if(downLeft)  result += SOUTH_WEST;

        return pick_tile.get(result);
    }
}
