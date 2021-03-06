/* Copyright (C) 2005-2011 Fabio Riccardi */
/* Copyright (C) 2016-     Masahiro Kitagawa */

package com.lightcrafts.image.metadata.makernotes;

import lombok.NoArgsConstructor;

/**
 * <code>OlympusMakerNoteProbe</code> is-a {@link MakerNoteProbe} for
 * determining whether a maker note is by Olympus.
 *
 * @author Paul J. Lucas [paul@lightcrafts.com]
 * @author Masahiro Kitagawa [arctica0316@gmail.com]
 */
@NoArgsConstructor(staticName = "create")
final class OlympusMakerNoteProbe extends MakerNoteProbe<OlympusDirectory>
{
    @Override
    protected Class<OlympusDirectory> getDirClass() {
        return OlympusDirectory.class;
    }
}
/* vim:set et sw=4 ts=4: */
