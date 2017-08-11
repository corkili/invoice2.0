package org.hld.invoice.common.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

public class ImageUtil {
    private static byte[] blobToBytes(Blob blob) {
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(blob.getBinaryStream());
            byte[] bytes = new byte[(int)blob.length()];
            int len = bytes.length;
            int offset = 0;
            int read = 0;
            while (offset < len && (read = is.read(bytes, offset, len - offset)) >= 0) {
                offset += read;
            }
            return bytes;
        } catch (SQLException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ignored) {

            }
        }
        return null;
    }

    public static BufferedImage BlobToImage(Blob blob) {
        byte[] bytes = blobToBytes(blob);
        if (bytes == null) {
            return null;
        } else {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            try {
                return ImageIO.read(in);
            } catch (IOException e) {
                return null;
            } finally {
                try {
                    in.close();
                } catch (IOException ignored) {

                }
            }
        }
    }
}
