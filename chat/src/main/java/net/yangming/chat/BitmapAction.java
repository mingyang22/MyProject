package net.yangming.chat;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

public class BitmapAction {
	static public Bitmap getBitmapFromFile(String sFile) {
		Bitmap bm = null;
		try {
			bm = BitmapFactory.decodeFile(sFile);
			int degree = readPictureDegree(sFile);
			bm = rotaingImageView(bm, degree);

		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return bm;
	}

	static public int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (Exception e) {
			degree = -1;
			e.printStackTrace();
		}
		return degree;
	}

	static private Bitmap rotaingImageView(Bitmap bitmap, int angle) {
		if (angle > 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(angle);

			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			return resizedBitmap;
		} else {
			return bitmap;
		}
	}

	static public Bitmap getBitmapFromFileLarge(String sFile, int w) {
		Bitmap bm = null;
		BitmapFactory.Options opts = null;
		if (w > 0) {
			opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(sFile, opts);
			int degree = readPictureDegree(sFile);

			int width = opts.outWidth;
			int height = opts.outHeight;
			if (degree == 90 || degree == 270) {
				height = opts.outWidth;
				width = opts.outHeight;
			}
			if (width > 0 && height > 0) {
				if (width < w) {
					bm = getBitmapFromFile(sFile);
				} else {
					int newWidth = w;
					int newHeight = height * w / width;

					// int minSideLength = Math.min(newWidth, newHeight);
					// opts.inSampleSize = 5; //computeSampleSize(opts,
					// minSideLength, newWidth * newHeight);

					opts.inSampleSize = width / newWidth;

					opts.inJustDecodeBounds = false;
					opts.inInputShareable = true;
					opts.inPurgeable = true;
					try {
						bm = BitmapFactory.decodeFile(sFile, opts);
					} catch (OutOfMemoryError e) {
						e.printStackTrace();
					}
				}

				bm = rotaingImageView(bm, degree);
			}
		}
		else {
			bm = getBitmapFromFile(sFile);
		}
		return bm;
	}

	static public Bitmap getBitmapFromFile(String sFile, int w) {
		Bitmap bm = null;
		BitmapFactory.Options opts = null;
		if (w > 0) {
			opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(sFile, opts);
			int degree = readPictureDegree(sFile);

			int width = opts.outWidth;
			int height = opts.outHeight;
			if (degree == 90 || degree == 270) {
				height = opts.outWidth;
				width = opts.outHeight;
			}
			if (width > 0 && height > 0) {
				if (width < w) {
					bm = getBitmapFromFile(sFile);
				} else {
					int newWidth = w;
					int newHeight = height * w / width;

					int minSideLength = Math.min(newWidth, newHeight);
					opts.inSampleSize = 5; // computeSampleSize(opts,
											// minSideLength, newWidth *
											// newHeight);
					opts.inJustDecodeBounds = false;
					opts.inInputShareable = true;
					opts.inPurgeable = true;
					try {
						bm = BitmapFactory.decodeFile(sFile, opts);
					} catch (OutOfMemoryError e) {
						e.printStackTrace();
					}
				}

				//bm = rotaingImageView(bm, degree);
			}
		}
		else {
			bm = getBitmapFromFile(sFile);
		}
		return bm;
	}

	static public Bitmap resizeImage(Bitmap bitmap, int w) {
		Bitmap bm = bitmap;
		if (bitmap != null && w > 0) {
			if (bitmap.getWidth() > w) {
				Bitmap BitmapOrg = bitmap;
				int width = BitmapOrg.getWidth();
				int height = BitmapOrg.getHeight();
				int newWidth = w;
				int newHeight = height * w / width;
				float scaleWidth = ((float) newWidth) / width;
				float scaleHeight = ((float) newHeight) / height;
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				try {
					bm = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height,
							matrix, true);
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
				}
			}
		}
		return bm;
	}

	static public int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	static private int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

//	static public void saveBitmap(String bitName, Bitmap bmp) {
//		File f = new File(bitName);
//
//		try {
////			f.createNewFile();
////			FileOutputStream fOut = null;
////			fOut = new FileOutputStream(f);
////			bmp.compress(Bitmap.CompressFormat.PNG, 80, fOut);
////			fOut.flush();
////			fOut.close();
//			
//			Utils.compressBmpToFile(bmp, f);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public static Bitmap toRoundCorner(Bitmap source, int radius) {
		int width = source.getWidth();
		int height = source.getHeight();


		Bitmap clipped = null;
		try {
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(Color.WHITE);
			clipped = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(clipped);
			canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius,
					paint);
			paint.setXfermode(new PorterDuffXfermode(
					android.graphics.PorterDuff.Mode.SRC_IN));
			canvas.drawBitmap(source, 0, 0, paint);
		}
		catch (Error e) {
		}
		catch (Exception e) {
		}
		finally {
			
		}

		return clipped;

	}

	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	private static final int COLORDRAWABLE_DIMENSION = 1;

	private static final int DEFAULT_BORDER_WIDTH = 0;
	private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

	public static Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		try {
			Bitmap bitmap;

			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
						COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				if (drawable.getIntrinsicWidth() <= 0
						|| drawable.getIntrinsicHeight() <= 0) {
					return null;
				}
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}

			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}
	
	
	/*
     * This method was copied from http://stackoverflow.com/a/10028267/694378.
     * The only modifications I've made are to remove a couple of Log
     * statements which could slow things down slightly.
     */
    static public Bitmap fastblur(Bitmap sentBitmap, int radius) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
    	Bitmap bitmap = null;
try{
        bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
	}
	catch (Error e) {
		
	}
catch(Exception e) {
	
}
        return (bitmap);

    }
}
