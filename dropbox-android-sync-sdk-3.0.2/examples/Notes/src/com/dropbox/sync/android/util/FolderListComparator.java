package com.dropbox.sync.android.util;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import com.dropbox.sync.android.DbxFileInfo;

/**
 * Comparator providing a reasonable sort order for displaying folder listings to
 * a user.  This comparator groups folders together, and properly deals with
 * international characters in paths.  This comparator will give a predictable
 * sort order for file info from multiple folders, but may not provide the ideal
 * user-displayed sort order for such a case.
 */
public class FolderListComparator implements Comparator<DbxFileInfo> {
    private final boolean isNameFirst;
    private final boolean isAscending;

    /**
     * Gets a comparator whose sort order places name before date.
     *
     * @param ascending whether the sort order should be ascending,
     *  vs. descending (reversed).
     */
    public static FolderListComparator getNameFirst(boolean ascending) {
        return new FolderListComparator(true, ascending);
    }

    /**
     * Gets a comparator whose sort order places name before date.
     *
     * @param ascending whether the sort order should be ascending,
     *  vs. descending (reversed).
     */
    public static FolderListComparator getDateFirst(boolean ascending) {
        return new FolderListComparator(false, ascending);
    }

    private FolderListComparator(boolean nameFirst, boolean ascending) {
        isNameFirst = nameFirst;
        isAscending = ascending;
    }

    @Override
    public int compare(DbxFileInfo lhs, DbxFileInfo rhs) {
        int rawCmp = rawCompare(lhs, rhs);
        return isAscending ? rawCmp : -rawCmp;
    }

    int rawCompare(DbxFileInfo lhs, DbxFileInfo rhs) {
        // Folders are always grouped together, Windows-style.
        if (lhs.isFolder != rhs.isFolder) {
            return lhs.isFolder ? -1 : 1;
        }

        // Name and date are next, in the configured order.
        if (isNameFirst) {
            int cmp = comparePaths(lhs, rhs);
            if (0 != cmp) {
                return cmp;
            }
            cmp = compareDates(lhs, rhs);
            if (0 != cmp) {
                return cmp;
            }
        } else {
            int cmp = compareDates(lhs, rhs);
            if (0 != cmp) {
                return cmp;
            }
            cmp = comparePaths(lhs, rhs);
            if (0 != cmp) {
                return cmp;
            }
        }

        // Use size as final qualifier, though names should be unique in a real
        // folder listing.
        long longcmp = lhs.size - rhs.size;
        if (0 != longcmp) {
            return longcmp < 0 ? -1 : 1;
        }

        return 0;
    }

    private int comparePaths(DbxFileInfo lhs, DbxFileInfo rhs) {
        Collator c = Collator.getInstance(Locale.getDefault());
        c.setStrength(Collator.SECONDARY); // Case-insensitive

        // TODO: If we want a good ordering of multi-folder listings, we
        // may need to do comparison on a per-path-element basis, otherwise
        // files can sort unpredictably with respect to subfolders due to the
        // ordering of characters with respect to /.  This is good enough for
        // the listing of a single folder.
        return c.compare(lhs.toString(), rhs.toString());
    }

    private int compareDates(DbxFileInfo lhs, DbxFileInfo rhs) {
        return lhs.modifiedTime.compareTo(rhs.modifiedTime);
    }
}