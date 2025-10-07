package com.github.sigrarr.lunisolarcalc.util.calccomposition;

@SuppressWarnings("unchecked")
abstract class KeyPaths {

    static <KeyT> KeyT[] empty() {
        return (KeyT[]) new Object[0];
    }

    static <KeyT> KeyT[] of(KeyT key) {
        return (KeyT[]) new Object[] { key };
    }

    static <KeyT> KeyT[] add(KeyT[] path, KeyT key) {
        KeyT[] newPath = (KeyT[]) new Object[path.length+1];
        System.arraycopy(path, 0, newPath, 0, path.length);
        newPath[path.length] = key;
        return newPath;
    }

    static <KeyT> boolean contains(KeyT[] path, KeyT key) {
        for (KeyT k : path)
            if (key.equals(k))
                return true;
        return false;
    }

    static <KeyT> KeyT[] takeFrom(KeyT[] path, KeyT key) {
        int i = -1;
        for (int k = path.length - 1; k >= 0; k--)
            if (key.equals(path[k])) {
                i = k;
                break;
            }
        if (i == -1)
            return empty();
        KeyT[] fragment = (KeyT[]) new Object[path.length - i];
        System.arraycopy(path, i, fragment, 0, path.length - i);
        return fragment;
    }
}
