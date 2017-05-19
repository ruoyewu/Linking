#include <jni.h>
#include <string>
#include <vector>
using namespace std;

const int UP = 1;
const int DOWN = -1;
const int LEFT = 2;
const int RIGHT = -2;
vector<vector<int> > mPaths;
int** mNum;
int* mStartP;
int* mEndP;
int mFrom;
int mEnd;
int mWidth;
int mHeight;

void go(int, vector<int>, int, int, int, int);
void DFS(vector<int>, int, int, int, int);

void go(int direct, vector<int> path, int x, int y, int od, int step){
    vector<int> p(path);
    if (direct != od){
        step ++;
    }
    if (x < mHeight && x >= 0 && y >= 0 && y < mWidth && direct != -od){
        if (mNum[x][y] == mFrom && x == mEndP[0] && y == mEndP[1]){
            p.push_back(direct);
            if (step < 4){
                mPaths.push_back(p);
            }
        } else if (mNum[x][y] == 0){
            p.push_back(direct);
            if (step < 4){
                DFS(p,x,y,direct,step);
            }
        }
    }
}

void DFS(vector<int> path, int x, int y, int od, int step){
    go(UP,path,x-1,y,od,step);
    go(DOWN,path,x+1,y,od,step);
    go(LEFT,path,x,y-1,od,step);
    go(RIGHT,path,x,y+1,od,step);
}

JNIEXPORT jintArray JNICALL
Java_com_wuruoye_linking2_widget_LinkingView_isLink(JNIEnv *env, jobject instance, jobjectArray num,
                                                    jint width, jint height, jintArray startP_,
                                                    jintArray endP_, jint start, jint end) {
    jint *startP = env->GetIntArrayElements(startP_, NULL);
    jint *endP = env->GetIntArrayElements(endP_, NULL);

    mNum = (int **) num;
    mFrom = start;
    mEnd = end;
    mStartP = startP;
    mEndP = endP;
    mWidth = width;
    mHeight = height;

    vector<int> path;
    DFS(path,mStartP[0],mStartP[1],0,0);

    env->ReleaseIntArrayElements(startP_, startP, 0);
    env->ReleaseIntArrayElements(endP_, endP, 0);

    int min = -1;
    for (int i = 0; i < mPaths.size(); ++i) {
        if (min == -1 || mPaths[i].size() < mPaths[min].size()){
            min =i;
        }
    }
    if (min != -1){
        int* p = new int[mPaths[min].size()];
        for (int i = 0; i < mPaths[min].size(); ++i) {
            p[i] = mPaths[min][i];
        }
        jintArray array = env->NewIntArray((jsize) mPaths[min].size());
        env->ReleaseIntArrayElements(array,p,0);
        return array;
    } else{
        return 0;
    }

}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_wuruoye_linking2_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
