// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import java.awt.event.KeyEvent;

public class KeyEventWork
{

    private static long lastKeyTime;
    private static int last;
    private static final int LAST_NOTHING = 0;
    private static final int LAST_ALT = 1;
    private static final int LAST_BROKEN = 2;
    private static final int LAST_NUMKEYPAD = 3;
    private static final int LAST_MOD = 4;

    public KeyEventWork()
    {
    }

    public static KeyEvent processKeyEvent(KeyEvent keyevent)
    {
        int i = keyevent.getKeyCode();
        char c = keyevent.getKeyChar();
        switch(keyevent.getID())
        {
        case 401: 
            switch(i)
            {
            case 0: // '\0'
            case 16: // '\020'
            case 17: // '\021'
            case 18: // '\022'
            case 128: 
            case 129: 
            case 130: 
            case 131: 
            case 132: 
            case 133: 
            case 134: 
            case 135: 
            case 136: 
            case 137: 
            case 138: 
            case 139: 
            case 140: 
            case 141: 
            case 142: 
            case 143: 
            case 157: 
            case 65406: 
                return null;
            }
            switch(i)
            {
            case 96: // '`'
            case 97: // 'a'
            case 98: // 'b'
            case 99: // 'c'
            case 100: // 'd'
            case 101: // 'e'
            case 102: // 'f'
            case 103: // 'g'
            case 104: // 'h'
            case 105: // 'i'
            case 106: // 'j'
            case 107: // 'k'
            case 109: // 'm'
            case 110: // 'n'
            case 111: // 'o'
                last = 3;
                lastKeyTime = System.currentTimeMillis();
                return keyevent;

            case 108: // 'l'
            default:
                handleBrokenKeys(keyevent, i);
                return keyevent;
            }

        case 400: 
            if((c < ' ' || c == '\177' || c == '\377' || c == '\b') && c != '\t')
                return null;
            if(keyevent.isControlDown() ^ keyevent.isAltDown() || keyevent.isMetaDown())
                return null;
            if(last == 4)
                switch(c)
                {
                case 33: // '!'
                case 44: // ','
                case 63: // '?'
                case 66: // 'B'
                case 77: // 'M'
                case 88: // 'X'
                case 99: // 'c'
                    last = 0;
                    return null;
                }
            if(last == 3 && System.currentTimeMillis() - lastKeyTime < 750L)
            {
                last = 0;
                if(c >= '0' && c <= '9' || c == '.' || c == '/' || c == '*' || c == '-' || c == '+')
                    return null;
            } else
            {
                if(last == 2 && System.currentTimeMillis() - lastKeyTime < 750L && !Character.isLetter(c))
                {
                    last = 0;
                    return null;
                }
                if(last == 1 && System.currentTimeMillis() - lastKeyTime < 750L)
                {
                    last = 0;
                    return null;
                }
            }
            return keyevent;

        case 402: 
            return keyevent;
        }
        return keyevent;
    }

    public static void numericKeypadKey()
    {
        last = 0;
    }

    private static void handleBrokenKeys(KeyEvent keyevent, int i)
    {
        if(keyevent.isAltDown() && keyevent.isControlDown() && !keyevent.isMetaDown())
        {
            last = 0;
            return;
        }
        if(!keyevent.isAltDown() && !keyevent.isControlDown() && !keyevent.isMetaDown())
        {
            last = 0;
            return;
        }
        if(keyevent.isAltDown())
            last = 1;
        switch(i)
        {
        case 8: // '\b'
        case 9: // '\t'
        case 10: // '\n'
        case 37: // '%'
        case 38: // '&'
        case 39: // '\''
        case 40: // '('
        case 127: // '\177'
            last = 0;
            break;

        default:
            if(i < 65 || i > 90)
                last = 2;
            else
                last = 0;
            break;
        }
        lastKeyTime = System.currentTimeMillis();
    }
}
