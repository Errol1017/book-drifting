package project.resource.pojo;

/**
 * Created by Errol on 17/6/13.
 */
public enum UploadFolders {

    avatar,
    doc,
    excel,
    img,
    qrcode,
    temp,
    zip,;

    public static String[] toArray() {
        UploadFolders[] arr = UploadFolders.values();
        String[] res = new String[arr.length];
        int i = 0;
        for (UploadFolders u : arr) {
            res[i++] = u.toString();
        }
        return res;
    }

}
