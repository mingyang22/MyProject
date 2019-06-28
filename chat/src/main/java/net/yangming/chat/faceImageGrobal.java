package net.yangming.chat;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.EditText;
import android.widget.TextView;

public class faceImageGrobal {

    static private String[] imagesStr = {"[微笑]", "[撇嘴]", "[色]", "[发呆]", "[得意]", "[流泪]", "[害羞]", "[闭嘴]", "[睡]", "[大哭]",
            "[尴尬]", "[发怒]", "[调皮]", "[龇牙]", "[惊讶]", "[难过]", "[酷]", "[冷汗]", "[抓狂]", "[吐]", "[偷笑]", "[可爱]", "[白眼]",
            "[傲慢]", "[饥饿]", "[困]", "[惊恐]", "[流汗]", "[憨笑]", "[大兵]", "[奋斗]", "[咒骂]", "[疑问]", "[嘘]", "[晕]", "[折磨]", "[衰]",
            "[骷髅]", "[敲打]", "[再见]", "[抠鼻]", "[鼓掌]", "[糗大了]", "[坏笑]", "[左哼哼]", "[右哼哼]", "[哈欠]", "[鄙视]", "[委屈]", "[快哭了]",
            "[阴险]", "[亲亲]", "[吓]", "[可怜]", "[菜刀]", "[西瓜]", "[啤酒]", "[篮球]", "[乒乓]", "[咖啡]", "[饭]", "[猪头]", "[玫瑰]",
            "[凋谢]", "[示爱]", "[爱心]", "[蛋糕]", "[闪电]", "[炸弹]", "[刀]", "[足球]", "[瓢虫]", "[便便]", "[月亮]", "[太阳]", "[礼物]",
            "[抱抱]", "[强]", "[弱]", "[握手]", "[胜利]", "[抱拳]", "[勾引]", "[拳头]", "[差劲]", "[爱你]", "[NO]", "[OK]", "[爱情]",
            "[飞吻]", "[发抖]", "[挥手]", "[钱]", "[美女]"};

    static private Integer[] images = {
            // 九宫格图片的设置
            R.mipmap.ic_1, R.mipmap.ic_2, R.mipmap.ic_3, R.mipmap.ic_4, R.mipmap.ic_5, R.mipmap.ic_6,
            R.mipmap.ic_7, R.mipmap.ic_8, R.mipmap.ic_9, R.mipmap.ic_10, R.mipmap.ic_11, R.mipmap.ic_12,
            R.mipmap.ic_13, R.mipmap.ic_14, R.mipmap.ic_15, R.mipmap.ic_16, R.mipmap.ic_17, R.mipmap.ic_18,
            R.mipmap.ic_19, R.mipmap.ic_20, R.mipmap.ic_21, R.mipmap.ic_22, R.mipmap.ic_23, R.mipmap.ic_24,
            R.mipmap.ic_25, R.mipmap.ic_26, R.mipmap.ic_27, R.mipmap.ic_28, R.mipmap.ic_29, R.mipmap.ic_30,
            R.mipmap.ic_31, R.mipmap.ic_32, R.mipmap.ic_33, R.mipmap.ic_34, R.mipmap.ic_35, R.mipmap.ic_36,
            R.mipmap.ic_37, R.mipmap.ic_38, R.mipmap.ic_39, R.mipmap.ic_40, R.mipmap.ic_41, R.mipmap.ic_42,
            R.mipmap.ic_43, R.mipmap.ic_44, R.mipmap.ic_45, R.mipmap.ic_46, R.mipmap.ic_47, R.mipmap.ic_48,
            R.mipmap.ic_49, R.mipmap.ic_50, R.mipmap.ic_51, R.mipmap.ic_52, R.mipmap.ic_53, R.mipmap.ic_54,
            R.mipmap.ic_55, R.mipmap.ic_56, R.mipmap.ic_57, R.mipmap.ic_58, R.mipmap.ic_59, R.mipmap.ic_60,
            R.mipmap.ic_61, R.mipmap.ic_62, R.mipmap.ic_63, R.mipmap.ic_64, R.mipmap.ic_65, R.mipmap.ic_66,
            R.mipmap.ic_67, R.mipmap.ic_68, R.mipmap.ic_69, R.mipmap.ic_70, R.mipmap.ic_71, R.mipmap.ic_72,
            R.mipmap.ic_73, R.mipmap.ic_74, R.mipmap.ic_75, R.mipmap.ic_76, R.mipmap.ic_77, R.mipmap.ic_78,
            R.mipmap.ic_79, R.mipmap.ic_80, R.mipmap.ic_81, R.mipmap.ic_82, R.mipmap.ic_83, R.mipmap.ic_84,
            R.mipmap.ic_85, R.mipmap.ic_86, R.mipmap.ic_87, R.mipmap.ic_88, R.mipmap.ic_89, R.mipmap.ic_90,
            R.mipmap.ic_91, R.mipmap.ic_92, R.mipmap.ic_93, R.mipmap.ic_94};

    // static private int iFaceCount = 0;

    // static public void init() {
    // iFaceCount = images.length;
    // if (iFaceCount > imagesStr.length) {
    // iFaceCount = imagesStr.length;
    // }
    // }

    static public int getImage(int iIndex) {
        int iRes = -1;
        if (iIndex >= 0 && iIndex < getImageCount()) {
            iRes = images[iIndex];
        }
        return iRes;
    }

    static public int getImageCount() {
        int iFaceCount = images.length;
        if (iFaceCount > imagesStr.length) {
            iFaceCount = imagesStr.length;
        }
        return iFaceCount;
    }

    static public SpannableString createFace(Context ct, int iIndex) {
        if (iIndex >= 0 && iIndex < getImageCount() && ct != null) {
            int iRes = images[iIndex];
            Bitmap bitmap = BitmapFactory.decodeResource(ct.getResources(), iRes);
            String sFace = imagesStr[iIndex];
            return createImage(ct, sFace, bitmap);
        }
        return null;
    }

    static private SpannableString createImage(Context ct, String sImage, Bitmap bitmap) {
        if (bitmap != null && ct != null) {
            ImageSpan imageSpan = new ImageSpan(ct, bitmap);
            SpannableString spannableString = new SpannableString(sImage);
            spannableString.setSpan(imageSpan, 0, sImage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
        return null;
    }

    static private int getFaceIndex(String sString) {
        int iIndex = -1;
        for (int i = 0; i < getImageCount(); i++) {
            if (imagesStr[i].compareTo(sString) == 0) {
                iIndex = i;
                break;
            }
        }
        return iIndex;
    }

    static public void showImage(Context ct, TextView tv, String strSrc) {
        showImage(ct, tv, strSrc, null, null);
    }

    static public void showImage(Context ct, TextView tv, String strSrc, String sImage) {
        showImage(ct, tv, strSrc, sImage, null);
    }

    static public void showImage(Context ct, TextView tv, String strSrc, String sImage, String sUrl) {
        if (strSrc != null) {
            findStringAction findObj = new findStringAction();
            String sText = strSrc;
            tv.setText("");
            boolean bRun = true;
            while (bRun) {
                bRun = false;
                findStringAction.findInfo obj3 = findObj.findBetween(sText, "[", "]");
                if (obj3 != null) {
                    if (obj3.iFrom > 0) {
                        tv.append(sText.substring(0, obj3.iFrom));
                    }
                    String sBetween = "[" + obj3.sBetween + "]";
                    int iIndex = getFaceIndex(sBetween);
                    if (iIndex >= 0) {
                        addFace(ct, tv, iIndex);
                    } else {
                        if (sBetween.compareToIgnoreCase("[image]") == 0) {
                            if (sImage != null) {
                                byte[] pIcon = Base64Action.decode(sImage, Base64Action.DEFAULT);
                                if (pIcon != null) {
                                    Bitmap bm = null;
                                    try {
                                        bm = BitmapFactory.decodeByteArray(pIcon, 0, pIcon.length);
                                    } catch (Exception e) {
                                    }
                                    if (bm != null) {

                                        Bitmap im = bm;
                                        int width = 480;
                                        if (bm.getWidth() > width / 3) {
                                            im = BitmapAction.resizeImage(bm, width / 3);
                                        }
                                        if (im != null) {
                                            addImage(ct, tv, "[image]", im);
                                        }
                                    }
                                }
                            }
                        } else {
                            tv.append(sBetween);
                        }
                    }
                    if (obj3.iTo < sText.length()) {
                        sText = sText.substring(obj3.iTo, sText.length());
                        bRun = true;
                    }
                } else {
                    tv.append(sText);
                }
            }
        }
    }

    static public void addFace(Context ct, EditText tv, int iIndex) {
        SpannableString ss = createFace(ct, iIndex);
        if (ss != null) {
            tv.append(ss);
        }
    }

    static public void addFace(Context ct, TextView tv, int iIndex) {
        SpannableString ss = createFace(ct, iIndex);
        if (ss != null) {
            tv.append(ss);
        }
    }

    static public void addImage(Context ct, EditText tv, String sImage, Bitmap bitmap) {
        SpannableString ss = createImage(ct, sImage, bitmap);
        if (ss != null) {
            tv.append(ss);
        }
    }

    static public void addImage(Context ct, TextView tv, String sImage, Bitmap bitmap) {
        SpannableString ss = createImage(ct, sImage, bitmap);
        if (ss != null) {
            tv.append(ss);
        }
    }

    // static public void addImage(Context ct, TextView tv, String sImage,
    // Bitmap bitmap, ImageDownloadClickableSpan idcs) {
    // if (bitmap != null && ct != null) {
    // ImageSpan imageSpan = new ImageSpan(ct, bitmap);
    // SpannableString spannableString = new SpannableString(sImage);
    // spannableString.setSpan(imageSpan, 0, sImage.length(),
    // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    // spannableString.setSpan(idcs, 0, sImage.length(),
    // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    // if (spannableString != null) {
    // tv.append(spannableString);
    // tv.setMovementMethod(LinkMovementMethod.getInstance());
    // }
    // }
    // }

}
