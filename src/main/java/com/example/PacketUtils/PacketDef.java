package com.example.PacketUtils;

import java.util.LinkedHashMap;

public class PacketDef {
    public final String name;
    public final LinkedHashMap<String, String> fields;
    public final PacketType type;

    public static final PacketDef OPOBJ1;
    public static final PacketDef IF_BUTTON9;
    public static final PacketDef IF_BUTTON8;
    public static final PacketDef OPOBJ5;
    public static final PacketDef IF_BUTTON5;
    public static final PacketDef OPOBJ4;
    public static final PacketDef IF_BUTTON4;
    public static final PacketDef OPOBJ3;
    public static final PacketDef IF_BUTTON7;
    public static final PacketDef OPOBJ2;
    public static final PacketDef IF_BUTTON6;
    public static final PacketDef OPLOCT;
    public static final PacketDef OPNPCT;
    public static final PacketDef OPPLAYERT;
    public static final PacketDef OPOBJT;
    public static final PacketDef IF_BUTTONT;
    public static final PacketDef OPNPC2;
    public static final PacketDef OPPLAYER6;
    public static final PacketDef OPNPC3;
    public static final PacketDef OPPLAYER7;
    public static final PacketDef OPLOC2;
    public static final PacketDef OPPLAYER8;
    public static final PacketDef OPLOC1;
    public static final PacketDef OPNPC1;
    public static final PacketDef OPLOC4;
    public static final PacketDef OPPLAYER2;
    public static final PacketDef OPLOC3;
    public static final PacketDef OPPLAYER3;
    public static final PacketDef OPNPC4;
    public static final PacketDef OPPLAYER4;
    public static final PacketDef OPNPC5;
    public static final PacketDef OPPLAYER5;
    public static final PacketDef OPLOC5;
    public static final PacketDef OPPLAYER1;
    public static final PacketDef MOVE_GAMECLICK;
    public static final PacketDef IF_BUTTON1;
    public static final PacketDef IF_BUTTON3;
    public static final PacketDef IF_BUTTON2;
    public static final PacketDef EVENT_MOUSE_CLICK;
    public static final PacketDef IF_BUTTON10;
    public static final PacketDef RESUME_PAUSEBUTTON;

    PacketDef(String var1, LinkedHashMap fields, PacketType type) {
        this.name = var1;
        this.fields = fields;
        this.type = type;
    }

    static {
        RESUME_PAUSEBUTTON = new PacketDef(ObfuscatedNames.RESUME_PAUSEBUTTON_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {{
            put(ObfuscatedNames.RESUME_PAUSEBUTTON_WRITE1, ObfuscatedNames.RESUME_PAUSEBUTTON_METHOD_NAME1);
            put(ObfuscatedNames.RESUME_PAUSEBUTTON_WRITE2, ObfuscatedNames.RESUME_PAUSEBUTTON_METHOD_NAME2);
        }}, PacketType.RESUME_PAUSEBUTTON);
        OPOBJ1 = new PacketDef(ObfuscatedNames.OPOBJ1_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPOBJ1_WRITE1, ObfuscatedNames.OPOBJ1_METHOD_NAME1);
                put(ObfuscatedNames.OPOBJ1_WRITE2, ObfuscatedNames.OPOBJ1_METHOD_NAME2);
                put(ObfuscatedNames.OPOBJ1_WRITE3, ObfuscatedNames.OPOBJ1_METHOD_NAME3);
                put(ObfuscatedNames.OPOBJ1_WRITE4, ObfuscatedNames.OPOBJ1_METHOD_NAME4);
            }
        }, PacketType.OPOBJ);
        IF_BUTTON9 = new PacketDef(ObfuscatedNames.IF_BUTTON9_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.IF_BUTTON9_WRITE1, ObfuscatedNames.IF_BUTTON9_METHOD_NAME1);
                put(ObfuscatedNames.IF_BUTTON9_WRITE2, ObfuscatedNames.IF_BUTTON9_METHOD_NAME2);
                put(ObfuscatedNames.IF_BUTTON9_WRITE3, ObfuscatedNames.IF_BUTTON9_METHOD_NAME3);
            }
        }, PacketType.IF_BUTTON);
        IF_BUTTON8 = new PacketDef(ObfuscatedNames.IF_BUTTON8_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.IF_BUTTON8_WRITE1, ObfuscatedNames.IF_BUTTON8_METHOD_NAME1);
                put(ObfuscatedNames.IF_BUTTON8_WRITE2, ObfuscatedNames.IF_BUTTON8_METHOD_NAME2);
                put(ObfuscatedNames.IF_BUTTON8_WRITE3, ObfuscatedNames.IF_BUTTON8_METHOD_NAME3);
            }
        }, PacketType.IF_BUTTON);
        OPOBJ5 = new PacketDef(ObfuscatedNames.OPOBJ5_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPOBJ5_WRITE1, ObfuscatedNames.OPOBJ5_METHOD_NAME1);
                put(ObfuscatedNames.OPOBJ5_WRITE2, ObfuscatedNames.OPOBJ5_METHOD_NAME2);
                put(ObfuscatedNames.OPOBJ5_WRITE3, ObfuscatedNames.OPOBJ5_METHOD_NAME3);
                put(ObfuscatedNames.OPOBJ5_WRITE4, ObfuscatedNames.OPOBJ5_METHOD_NAME4);
            }
        }, PacketType.OPOBJ);
        IF_BUTTON5 = new PacketDef(ObfuscatedNames.IF_BUTTON5_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.IF_BUTTON5_WRITE1, ObfuscatedNames.IF_BUTTON5_METHOD_NAME1);
                put(ObfuscatedNames.IF_BUTTON5_WRITE2, ObfuscatedNames.IF_BUTTON5_METHOD_NAME2);
                put(ObfuscatedNames.IF_BUTTON5_WRITE3, ObfuscatedNames.IF_BUTTON5_METHOD_NAME3);
            }
        }, PacketType.IF_BUTTON);
        OPOBJ4 = new PacketDef(ObfuscatedNames.OPOBJ4_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPOBJ4_WRITE1, ObfuscatedNames.OPOBJ4_METHOD_NAME1);
                put(ObfuscatedNames.OPOBJ4_WRITE2, ObfuscatedNames.OPOBJ4_METHOD_NAME2);
                put(ObfuscatedNames.OPOBJ4_WRITE3, ObfuscatedNames.OPOBJ4_METHOD_NAME3);
                put(ObfuscatedNames.OPOBJ4_WRITE4, ObfuscatedNames.OPOBJ4_METHOD_NAME4);
            }
        }, PacketType.OPOBJ);
        IF_BUTTON4 = new PacketDef(ObfuscatedNames.IF_BUTTON4_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.IF_BUTTON4_WRITE1, ObfuscatedNames.IF_BUTTON4_METHOD_NAME1);
                put(ObfuscatedNames.IF_BUTTON4_WRITE2, ObfuscatedNames.IF_BUTTON4_METHOD_NAME2);
                put(ObfuscatedNames.IF_BUTTON4_WRITE3, ObfuscatedNames.IF_BUTTON4_METHOD_NAME3);
            }
        }, PacketType.IF_BUTTON);
        OPOBJ3 = new PacketDef(ObfuscatedNames.OPOBJ3_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPOBJ3_WRITE1, ObfuscatedNames.OPOBJ3_METHOD_NAME1);
                put(ObfuscatedNames.OPOBJ3_WRITE2, ObfuscatedNames.OPOBJ3_METHOD_NAME2);
                put(ObfuscatedNames.OPOBJ3_WRITE3, ObfuscatedNames.OPOBJ3_METHOD_NAME3);
                put(ObfuscatedNames.OPOBJ3_WRITE4, ObfuscatedNames.OPOBJ3_METHOD_NAME4);
            }
        }, PacketType.OPOBJ);
        IF_BUTTON7 = new PacketDef(ObfuscatedNames.IF_BUTTON7_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.IF_BUTTON7_WRITE1, ObfuscatedNames.IF_BUTTON7_METHOD_NAME1);
                put(ObfuscatedNames.IF_BUTTON7_WRITE2, ObfuscatedNames.IF_BUTTON7_METHOD_NAME2);
                put(ObfuscatedNames.IF_BUTTON7_WRITE3, ObfuscatedNames.IF_BUTTON7_METHOD_NAME3);
            }
        }, PacketType.IF_BUTTON);
        OPOBJ2 = new PacketDef(ObfuscatedNames.OPOBJ2_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPOBJ2_WRITE1, ObfuscatedNames.OPOBJ2_METHOD_NAME1);
                put(ObfuscatedNames.OPOBJ2_WRITE2, ObfuscatedNames.OPOBJ2_METHOD_NAME2);
                put(ObfuscatedNames.OPOBJ2_WRITE3, ObfuscatedNames.OPOBJ2_METHOD_NAME3);
                put(ObfuscatedNames.OPOBJ2_WRITE4, ObfuscatedNames.OPOBJ2_METHOD_NAME4);
            }
        }, PacketType.OPOBJ);
        IF_BUTTON6 = new PacketDef(ObfuscatedNames.IF_BUTTON6_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.IF_BUTTON6_WRITE1, ObfuscatedNames.IF_BUTTON6_METHOD_NAME1);
                put(ObfuscatedNames.IF_BUTTON6_WRITE2, ObfuscatedNames.IF_BUTTON6_METHOD_NAME2);
                put(ObfuscatedNames.IF_BUTTON6_WRITE3, ObfuscatedNames.IF_BUTTON6_METHOD_NAME3);
            }
        }, PacketType.IF_BUTTON);
        OPLOCT = new PacketDef(ObfuscatedNames.OPLOCT_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPLOCT_WRITE1, ObfuscatedNames.OPLOCT_METHOD_NAME1);
                put(ObfuscatedNames.OPLOCT_WRITE2, ObfuscatedNames.OPLOCT_METHOD_NAME2);
                put(ObfuscatedNames.OPLOCT_WRITE3, ObfuscatedNames.OPLOCT_METHOD_NAME3);
                put(ObfuscatedNames.OPLOCT_WRITE4, ObfuscatedNames.OPLOCT_METHOD_NAME4);
                put(ObfuscatedNames.OPLOCT_WRITE5, ObfuscatedNames.OPLOCT_METHOD_NAME5);
                put(ObfuscatedNames.OPLOCT_WRITE6, ObfuscatedNames.OPLOCT_METHOD_NAME6);
                put(ObfuscatedNames.OPLOCT_WRITE7, ObfuscatedNames.OPLOCT_METHOD_NAME7);
            }
        }, PacketType.OPLOCT);
        OPNPCT = new PacketDef(ObfuscatedNames.OPNPCT_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPNPCT_WRITE1, ObfuscatedNames.OPNPCT_METHOD_NAME1);
                put(ObfuscatedNames.OPNPCT_WRITE2, ObfuscatedNames.OPNPCT_METHOD_NAME2);
                put(ObfuscatedNames.OPNPCT_WRITE3, ObfuscatedNames.OPNPCT_METHOD_NAME3);
                put(ObfuscatedNames.OPNPCT_WRITE4, ObfuscatedNames.OPNPCT_METHOD_NAME4);
                put(ObfuscatedNames.OPNPCT_WRITE5, ObfuscatedNames.OPNPCT_METHOD_NAME5);
            }
        }, PacketType.OPNPCT);
        OPPLAYERT = new PacketDef(ObfuscatedNames.OPPLAYERT_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPPLAYERT_WRITE1, ObfuscatedNames.OPPLAYERT_METHOD_NAME1);
                put(ObfuscatedNames.OPPLAYERT_WRITE2, ObfuscatedNames.OPPLAYERT_METHOD_NAME2);
                put(ObfuscatedNames.OPPLAYERT_WRITE3, ObfuscatedNames.OPPLAYERT_METHOD_NAME3);
                put(ObfuscatedNames.OPPLAYERT_WRITE4, ObfuscatedNames.OPPLAYERT_METHOD_NAME4);
                put(ObfuscatedNames.OPPLAYERT_WRITE5, ObfuscatedNames.OPPLAYERT_METHOD_NAME5);
            }
        }, PacketType.OPPLAYERT);
        OPOBJT = new PacketDef(ObfuscatedNames.OPOBJT_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPOBJT_WRITE1, ObfuscatedNames.OPOBJT_METHOD_NAME1);
                put(ObfuscatedNames.OPOBJT_WRITE2, ObfuscatedNames.OPOBJT_METHOD_NAME2);
                put(ObfuscatedNames.OPOBJT_WRITE3, ObfuscatedNames.OPOBJT_METHOD_NAME3);
                put(ObfuscatedNames.OPOBJT_WRITE4, ObfuscatedNames.OPOBJT_METHOD_NAME4);
                put(ObfuscatedNames.OPOBJT_WRITE5, ObfuscatedNames.OPOBJT_METHOD_NAME5);
                put(ObfuscatedNames.OPOBJT_WRITE6, ObfuscatedNames.OPOBJT_METHOD_NAME6);
                put(ObfuscatedNames.OPOBJT_WRITE7, ObfuscatedNames.OPOBJT_METHOD_NAME7);
            }
        }, PacketType.OPOBJT);
        IF_BUTTONT = new PacketDef(ObfuscatedNames.IF_BUTTONT_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.IF_BUTTONT_WRITE1, ObfuscatedNames.IF_BUTTONT_METHOD_NAME1);
                put(ObfuscatedNames.IF_BUTTONT_WRITE2, ObfuscatedNames.IF_BUTTONT_METHOD_NAME2);
                put(ObfuscatedNames.IF_BUTTONT_WRITE3, ObfuscatedNames.IF_BUTTONT_METHOD_NAME3);
                put(ObfuscatedNames.IF_BUTTONT_WRITE4, ObfuscatedNames.IF_BUTTONT_METHOD_NAME4);
                put(ObfuscatedNames.IF_BUTTONT_WRITE5, ObfuscatedNames.IF_BUTTONT_METHOD_NAME5);
                put(ObfuscatedNames.IF_BUTTONT_WRITE6, ObfuscatedNames.IF_BUTTONT_METHOD_NAME6);
            }
        }, PacketType.IF_BUTTONT);
        OPNPC2 = new PacketDef(ObfuscatedNames.OPNPC2_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPNPC2_WRITE1, ObfuscatedNames.OPNPC2_METHOD_NAME1);
                put(ObfuscatedNames.OPNPC2_WRITE2, ObfuscatedNames.OPNPC2_METHOD_NAME2);
            }
        }, PacketType.OPNPC);
        OPPLAYER6 = new PacketDef(ObfuscatedNames.OPPLAYER6_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPPLAYER6_WRITE1, ObfuscatedNames.OPPLAYER6_METHOD_NAME1);
                put(ObfuscatedNames.OPPLAYER6_WRITE2, ObfuscatedNames.OPPLAYER6_METHOD_NAME2);
            }
        }, PacketType.OPPLAYER);
        OPNPC3 = new PacketDef(ObfuscatedNames.OPNPC3_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPNPC3_WRITE1, ObfuscatedNames.OPNPC3_METHOD_NAME1);
                put(ObfuscatedNames.OPNPC3_WRITE2, ObfuscatedNames.OPNPC3_METHOD_NAME2);
            }
        }, PacketType.OPNPC);
        OPPLAYER7 = new PacketDef(ObfuscatedNames.OPPLAYER7_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPPLAYER7_WRITE1, ObfuscatedNames.OPPLAYER7_METHOD_NAME1);
                put(ObfuscatedNames.OPPLAYER7_WRITE2, ObfuscatedNames.OPPLAYER7_METHOD_NAME2);
            }
        }, PacketType.OPPLAYER);
        OPLOC2 = new PacketDef(ObfuscatedNames.OPLOC2_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPLOC2_WRITE1, ObfuscatedNames.OPLOC2_METHOD_NAME1);
                put(ObfuscatedNames.OPLOC2_WRITE2, ObfuscatedNames.OPLOC2_METHOD_NAME2);
                put(ObfuscatedNames.OPLOC2_WRITE3, ObfuscatedNames.OPLOC2_METHOD_NAME3);
                put(ObfuscatedNames.OPLOC2_WRITE4, ObfuscatedNames.OPLOC2_METHOD_NAME4);
            }
        }, PacketType.OPLOC);
        OPPLAYER8 = new PacketDef(ObfuscatedNames.OPPLAYER8_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPPLAYER8_WRITE1, ObfuscatedNames.OPPLAYER8_METHOD_NAME1);
                put(ObfuscatedNames.OPPLAYER8_WRITE2, ObfuscatedNames.OPPLAYER8_METHOD_NAME2);
            }
        }, PacketType.OPPLAYER);
        OPLOC1 = new PacketDef(ObfuscatedNames.OPLOC1_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPLOC1_WRITE1, ObfuscatedNames.OPLOC1_METHOD_NAME1);
                put(ObfuscatedNames.OPLOC1_WRITE2, ObfuscatedNames.OPLOC1_METHOD_NAME2);
                put(ObfuscatedNames.OPLOC1_WRITE3, ObfuscatedNames.OPLOC1_METHOD_NAME3);
                put(ObfuscatedNames.OPLOC1_WRITE4, ObfuscatedNames.OPLOC1_METHOD_NAME4);
            }
        }, PacketType.OPLOC);
        OPNPC1 = new PacketDef(ObfuscatedNames.OPNPC1_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPNPC1_WRITE1, ObfuscatedNames.OPNPC1_METHOD_NAME1);
                put(ObfuscatedNames.OPNPC1_WRITE2, ObfuscatedNames.OPNPC1_METHOD_NAME2);
            }
        }, PacketType.OPNPC);
        OPLOC4 = new PacketDef(ObfuscatedNames.OPLOC4_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPLOC4_WRITE1, ObfuscatedNames.OPLOC4_METHOD_NAME1);
                put(ObfuscatedNames.OPLOC4_WRITE2, ObfuscatedNames.OPLOC4_METHOD_NAME2);
                put(ObfuscatedNames.OPLOC4_WRITE3, ObfuscatedNames.OPLOC4_METHOD_NAME3);
                put(ObfuscatedNames.OPLOC4_WRITE4, ObfuscatedNames.OPLOC4_METHOD_NAME4);
            }
        }, PacketType.OPLOC);
        OPPLAYER2 = new PacketDef(ObfuscatedNames.OPPLAYER2_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPPLAYER2_WRITE1, ObfuscatedNames.OPPLAYER2_METHOD_NAME1);
                put(ObfuscatedNames.OPPLAYER2_WRITE2, ObfuscatedNames.OPPLAYER2_METHOD_NAME2);
            }
        }, PacketType.OPPLAYER);
        OPLOC3 = new PacketDef(ObfuscatedNames.OPLOC3_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPLOC3_WRITE1, ObfuscatedNames.OPLOC3_METHOD_NAME1);
                put(ObfuscatedNames.OPLOC3_WRITE2, ObfuscatedNames.OPLOC3_METHOD_NAME2);
                put(ObfuscatedNames.OPLOC3_WRITE3, ObfuscatedNames.OPLOC3_METHOD_NAME3);
                put(ObfuscatedNames.OPLOC3_WRITE4, ObfuscatedNames.OPLOC3_METHOD_NAME4);
            }
        }, PacketType.OPLOC);
        OPPLAYER3 = new PacketDef(ObfuscatedNames.OPPLAYER3_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPPLAYER3_WRITE1, ObfuscatedNames.OPPLAYER3_METHOD_NAME1);
                put(ObfuscatedNames.OPPLAYER3_WRITE2, ObfuscatedNames.OPPLAYER3_METHOD_NAME2);
            }
        }, PacketType.OPPLAYER);
        OPNPC4 = new PacketDef(ObfuscatedNames.OPNPC4_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPNPC4_WRITE1, ObfuscatedNames.OPNPC4_METHOD_NAME1);
                put(ObfuscatedNames.OPNPC4_WRITE2, ObfuscatedNames.OPNPC4_METHOD_NAME2);
            }
        }, PacketType.OPNPC);
        OPPLAYER4 = new PacketDef(ObfuscatedNames.OPPLAYER4_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPPLAYER4_WRITE1, ObfuscatedNames.OPPLAYER4_METHOD_NAME1);
                put(ObfuscatedNames.OPPLAYER4_WRITE2, ObfuscatedNames.OPPLAYER4_METHOD_NAME2);
            }
        }, PacketType.OPPLAYER);
        OPNPC5 = new PacketDef(ObfuscatedNames.OPNPC5_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPNPC5_WRITE1, ObfuscatedNames.OPNPC5_METHOD_NAME1);
                put(ObfuscatedNames.OPNPC5_WRITE2, ObfuscatedNames.OPNPC5_METHOD_NAME2);
            }
        }, PacketType.OPNPC);
        OPPLAYER5 = new PacketDef(ObfuscatedNames.OPPLAYER5_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPPLAYER5_WRITE1, ObfuscatedNames.OPPLAYER5_METHOD_NAME1);
                put(ObfuscatedNames.OPPLAYER5_WRITE2, ObfuscatedNames.OPPLAYER5_METHOD_NAME2);
            }
        }, PacketType.OPPLAYER);
        OPLOC5 = new PacketDef(ObfuscatedNames.OPLOC5_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPLOC5_WRITE1, ObfuscatedNames.OPLOC5_METHOD_NAME1);
                put(ObfuscatedNames.OPLOC5_WRITE2, ObfuscatedNames.OPLOC5_METHOD_NAME2);
                put(ObfuscatedNames.OPLOC5_WRITE3, ObfuscatedNames.OPLOC5_METHOD_NAME3);
                put(ObfuscatedNames.OPLOC5_WRITE4, ObfuscatedNames.OPLOC5_METHOD_NAME4);
            }
        }, PacketType.OPLOC);
        OPPLAYER1 = new PacketDef(ObfuscatedNames.OPPLAYER1_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.OPPLAYER1_WRITE1, ObfuscatedNames.OPPLAYER1_METHOD_NAME1);
                put(ObfuscatedNames.OPPLAYER1_WRITE2, ObfuscatedNames.OPPLAYER1_METHOD_NAME2);
            }
        }, PacketType.OPPLAYER);
        MOVE_GAMECLICK = new PacketDef(ObfuscatedNames.MOVE_GAMECLICK_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.MOVE_GAMECLICK_WRITE1, ObfuscatedNames.MOVE_GAMECLICK_METHOD_NAME1);
                put(ObfuscatedNames.MOVE_GAMECLICK_WRITE2, ObfuscatedNames.MOVE_GAMECLICK_METHOD_NAME2);
                put(ObfuscatedNames.MOVE_GAMECLICK_WRITE3, ObfuscatedNames.MOVE_GAMECLICK_METHOD_NAME3);
                put(ObfuscatedNames.MOVE_GAMECLICK_WRITE4, ObfuscatedNames.MOVE_GAMECLICK_METHOD_NAME4);
            }
        }, PacketType.MOVE_GAMECLICK);
        IF_BUTTON1 = new PacketDef(ObfuscatedNames.IF_BUTTON1_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.IF_BUTTON1_WRITE1, ObfuscatedNames.IF_BUTTON1_METHOD_NAME1);
                put(ObfuscatedNames.IF_BUTTON1_WRITE2, ObfuscatedNames.IF_BUTTON1_METHOD_NAME2);
                put(ObfuscatedNames.IF_BUTTON1_WRITE3, ObfuscatedNames.IF_BUTTON1_METHOD_NAME3);
            }
        }, PacketType.IF_BUTTON);
        IF_BUTTON3 = new PacketDef(ObfuscatedNames.IF_BUTTON3_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.IF_BUTTON3_WRITE1, ObfuscatedNames.IF_BUTTON3_METHOD_NAME1);
                put(ObfuscatedNames.IF_BUTTON3_WRITE2, ObfuscatedNames.IF_BUTTON3_METHOD_NAME2);
                put(ObfuscatedNames.IF_BUTTON3_WRITE3, ObfuscatedNames.IF_BUTTON3_METHOD_NAME3);
            }
        }, PacketType.IF_BUTTON);
        IF_BUTTON2 = new PacketDef(ObfuscatedNames.IF_BUTTON2_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.IF_BUTTON2_WRITE1, ObfuscatedNames.IF_BUTTON2_METHOD_NAME1);
                put(ObfuscatedNames.IF_BUTTON2_WRITE2, ObfuscatedNames.IF_BUTTON2_METHOD_NAME2);
                put(ObfuscatedNames.IF_BUTTON2_WRITE3, ObfuscatedNames.IF_BUTTON2_METHOD_NAME3);
            }
        }, PacketType.IF_BUTTON);
        EVENT_MOUSE_CLICK = new PacketDef(ObfuscatedNames.EVENT_MOUSE_CLICK_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.EVENT_MOUSE_CLICK_WRITE1, ObfuscatedNames.EVENT_MOUSE_CLICK_METHOD_NAME1);
                put(ObfuscatedNames.EVENT_MOUSE_CLICK_WRITE2, ObfuscatedNames.EVENT_MOUSE_CLICK_METHOD_NAME2);
                put(ObfuscatedNames.EVENT_MOUSE_CLICK_WRITE3, ObfuscatedNames.EVENT_MOUSE_CLICK_METHOD_NAME3);
            }
        }, PacketType.EVENT_MOUSE_CLICK);
        IF_BUTTON10 = new PacketDef(ObfuscatedNames.IF_BUTTON10_OBFUSCATEDNAME, new LinkedHashMap<String, String>() {
            {
                put(ObfuscatedNames.IF_BUTTON10_WRITE1, ObfuscatedNames.IF_BUTTON10_METHOD_NAME1);
                put(ObfuscatedNames.IF_BUTTON10_WRITE2, ObfuscatedNames.IF_BUTTON10_METHOD_NAME2);
                put(ObfuscatedNames.IF_BUTTON10_WRITE3, ObfuscatedNames.IF_BUTTON10_METHOD_NAME3);
            }
        }, PacketType.IF_BUTTON);
    }
}
