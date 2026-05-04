package androidx.constraintlayout.core.motion.utils;

import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class ArcCurveFit extends CurveFit {
    public static final int ARC_START_FLIP = 3;
    public static final int ARC_START_HORIZONTAL = 2;
    public static final int ARC_START_LINEAR = 0;
    public static final int ARC_START_VERTICAL = 1;
    private static final int START_HORIZONTAL = 2;
    private static final int START_LINEAR = 3;
    private static final int START_VERTICAL = 1;
    Arc[] mArcs;
    private boolean mExtrapolate = true;
    private final double[] mTime;

    @Override // androidx.constraintlayout.core.motion.utils.CurveFit
    public void getPos(double t, double[] v) {
        boolean z = this.mExtrapolate;
        Arc[] arcArr = this.mArcs;
        if (z) {
            double d = arcArr[0].mTime1;
            Arc[] arcArr2 = this.mArcs;
            if (t < d) {
                double t0 = arcArr2[0].mTime1;
                double dt = t - this.mArcs[0].mTime1;
                boolean z2 = this.mArcs[0].linear;
                Arc[] arcArr3 = this.mArcs;
                if (z2) {
                    v[0] = arcArr3[0].getLinearX(t0) + (this.mArcs[0].getLinearDX(t0) * dt);
                    v[1] = this.mArcs[0].getLinearY(t0) + (this.mArcs[0].getLinearDY(t0) * dt);
                    return;
                } else {
                    arcArr3[0].setPoint(t0);
                    v[0] = this.mArcs[0].getX() + (this.mArcs[0].getDX() * dt);
                    v[1] = this.mArcs[0].getY() + (this.mArcs[0].getDY() * dt);
                    return;
                }
            }
            if (t > arcArr2[this.mArcs.length - 1].mTime2) {
                double t02 = this.mArcs[this.mArcs.length - 1].mTime2;
                double dt2 = t - t02;
                int p = this.mArcs.length - 1;
                boolean z3 = this.mArcs[p].linear;
                Arc[] arcArr4 = this.mArcs;
                if (z3) {
                    v[0] = arcArr4[p].getLinearX(t02) + (this.mArcs[p].getLinearDX(t02) * dt2);
                    v[1] = this.mArcs[p].getLinearY(t02) + (this.mArcs[p].getLinearDY(t02) * dt2);
                    return;
                } else {
                    arcArr4[p].setPoint(t);
                    v[0] = this.mArcs[p].getX() + (this.mArcs[p].getDX() * dt2);
                    v[1] = this.mArcs[p].getY() + (this.mArcs[p].getDY() * dt2);
                    return;
                }
            }
        } else {
            if (t < arcArr[0].mTime1) {
                t = this.mArcs[0].mTime1;
            }
            if (t > this.mArcs[this.mArcs.length - 1].mTime2) {
                t = this.mArcs[this.mArcs.length - 1].mTime2;
            }
        }
        for (int i = 0; i < this.mArcs.length; i++) {
            if (t <= this.mArcs[i].mTime2) {
                boolean z4 = this.mArcs[i].linear;
                Arc[] arcArr5 = this.mArcs;
                if (z4) {
                    v[0] = arcArr5[i].getLinearX(t);
                    v[1] = this.mArcs[i].getLinearY(t);
                    return;
                } else {
                    arcArr5[i].setPoint(t);
                    v[0] = this.mArcs[i].getX();
                    v[1] = this.mArcs[i].getY();
                    return;
                }
            }
        }
    }

    @Override // androidx.constraintlayout.core.motion.utils.CurveFit
    public void getPos(double t, float[] v) {
        boolean z = this.mExtrapolate;
        Arc[] arcArr = this.mArcs;
        if (z) {
            double d = arcArr[0].mTime1;
            Arc[] arcArr2 = this.mArcs;
            if (t < d) {
                double t0 = arcArr2[0].mTime1;
                double dt = t - this.mArcs[0].mTime1;
                boolean z2 = this.mArcs[0].linear;
                Arc[] arcArr3 = this.mArcs;
                if (z2) {
                    v[0] = (float) (arcArr3[0].getLinearX(t0) + (this.mArcs[0].getLinearDX(t0) * dt));
                    v[1] = (float) (this.mArcs[0].getLinearY(t0) + (this.mArcs[0].getLinearDY(t0) * dt));
                    return;
                } else {
                    arcArr3[0].setPoint(t0);
                    v[0] = (float) (this.mArcs[0].getX() + (this.mArcs[0].getDX() * dt));
                    v[1] = (float) (this.mArcs[0].getY() + (this.mArcs[0].getDY() * dt));
                    return;
                }
            }
            if (t > arcArr2[this.mArcs.length - 1].mTime2) {
                double t02 = this.mArcs[this.mArcs.length - 1].mTime2;
                double dt2 = t - t02;
                int p = this.mArcs.length - 1;
                boolean z3 = this.mArcs[p].linear;
                Arc[] arcArr4 = this.mArcs;
                if (z3) {
                    v[0] = (float) (arcArr4[p].getLinearX(t02) + (this.mArcs[p].getLinearDX(t02) * dt2));
                    v[1] = (float) (this.mArcs[p].getLinearY(t02) + (this.mArcs[p].getLinearDY(t02) * dt2));
                    return;
                } else {
                    arcArr4[p].setPoint(t);
                    v[0] = (float) this.mArcs[p].getX();
                    v[1] = (float) this.mArcs[p].getY();
                    return;
                }
            }
        } else {
            double d2 = arcArr[0].mTime1;
            Arc[] arcArr5 = this.mArcs;
            if (t < d2) {
                t = arcArr5[0].mTime1;
            } else if (t > arcArr5[this.mArcs.length - 1].mTime2) {
                t = this.mArcs[this.mArcs.length - 1].mTime2;
            }
        }
        for (int i = 0; i < this.mArcs.length; i++) {
            if (t <= this.mArcs[i].mTime2) {
                boolean z4 = this.mArcs[i].linear;
                Arc[] arcArr6 = this.mArcs;
                if (z4) {
                    v[0] = (float) arcArr6[i].getLinearX(t);
                    v[1] = (float) this.mArcs[i].getLinearY(t);
                    return;
                } else {
                    arcArr6[i].setPoint(t);
                    v[0] = (float) this.mArcs[i].getX();
                    v[1] = (float) this.mArcs[i].getY();
                    return;
                }
            }
        }
    }

    @Override // androidx.constraintlayout.core.motion.utils.CurveFit
    public void getSlope(double t, double[] v) {
        double d = this.mArcs[0].mTime1;
        Arc[] arcArr = this.mArcs;
        if (t < d) {
            t = arcArr[0].mTime1;
        } else if (t > arcArr[this.mArcs.length - 1].mTime2) {
            t = this.mArcs[this.mArcs.length - 1].mTime2;
        }
        for (int i = 0; i < this.mArcs.length; i++) {
            if (t <= this.mArcs[i].mTime2) {
                boolean z = this.mArcs[i].linear;
                Arc[] arcArr2 = this.mArcs;
                if (z) {
                    v[0] = arcArr2[i].getLinearDX(t);
                    v[1] = this.mArcs[i].getLinearDY(t);
                    return;
                } else {
                    arcArr2[i].setPoint(t);
                    v[0] = this.mArcs[i].getDX();
                    v[1] = this.mArcs[i].getDY();
                    return;
                }
            }
        }
    }

    @Override // androidx.constraintlayout.core.motion.utils.CurveFit
    public double getPos(double t, int j) {
        boolean z = this.mExtrapolate;
        Arc[] arcArr = this.mArcs;
        if (z) {
            double d = arcArr[0].mTime1;
            Arc[] arcArr2 = this.mArcs;
            if (t < d) {
                double t0 = arcArr2[0].mTime1;
                double dt = t - this.mArcs[0].mTime1;
                if (!this.mArcs[0].linear) {
                    this.mArcs[0].setPoint(t0);
                    Arc[] arcArr3 = this.mArcs;
                    return j == 0 ? arcArr3[0].getX() + (this.mArcs[0].getDX() * dt) : arcArr3[0].getY() + (this.mArcs[0].getDY() * dt);
                }
                Arc[] arcArr4 = this.mArcs;
                return j == 0 ? arcArr4[0].getLinearX(t0) + (this.mArcs[0].getLinearDX(t0) * dt) : arcArr4[0].getLinearY(t0) + (this.mArcs[0].getLinearDY(t0) * dt);
            }
            if (t > arcArr2[this.mArcs.length - 1].mTime2) {
                double t02 = this.mArcs[this.mArcs.length - 1].mTime2;
                double dt2 = t - t02;
                int p = this.mArcs.length - 1;
                Arc[] arcArr5 = this.mArcs;
                if (j == 0) {
                    return arcArr5[p].getLinearX(t02) + (this.mArcs[p].getLinearDX(t02) * dt2);
                }
                return arcArr5[p].getLinearY(t02) + (this.mArcs[p].getLinearDY(t02) * dt2);
            }
        } else {
            double d2 = arcArr[0].mTime1;
            Arc[] arcArr6 = this.mArcs;
            if (t < d2) {
                t = arcArr6[0].mTime1;
            } else if (t > arcArr6[this.mArcs.length - 1].mTime2) {
                t = this.mArcs[this.mArcs.length - 1].mTime2;
            }
        }
        for (int i = 0; i < this.mArcs.length; i++) {
            if (t <= this.mArcs[i].mTime2) {
                if (this.mArcs[i].linear) {
                    Arc[] arcArr7 = this.mArcs;
                    if (j == 0) {
                        return arcArr7[i].getLinearX(t);
                    }
                    return arcArr7[i].getLinearY(t);
                }
                this.mArcs[i].setPoint(t);
                Arc[] arcArr8 = this.mArcs;
                if (j == 0) {
                    return arcArr8[i].getX();
                }
                return arcArr8[i].getY();
            }
        }
        return Double.NaN;
    }

    @Override // androidx.constraintlayout.core.motion.utils.CurveFit
    public double getSlope(double t, int j) {
        if (t < this.mArcs[0].mTime1) {
            t = this.mArcs[0].mTime1;
        }
        if (t > this.mArcs[this.mArcs.length - 1].mTime2) {
            t = this.mArcs[this.mArcs.length - 1].mTime2;
        }
        for (int i = 0; i < this.mArcs.length; i++) {
            if (t <= this.mArcs[i].mTime2) {
                if (this.mArcs[i].linear) {
                    Arc[] arcArr = this.mArcs;
                    if (j == 0) {
                        return arcArr[i].getLinearDX(t);
                    }
                    return arcArr[i].getLinearDY(t);
                }
                this.mArcs[i].setPoint(t);
                Arc[] arcArr2 = this.mArcs;
                if (j == 0) {
                    return arcArr2[i].getDX();
                }
                return arcArr2[i].getDY();
            }
        }
        return Double.NaN;
    }

    @Override // androidx.constraintlayout.core.motion.utils.CurveFit
    public double[] getTimePoints() {
        return this.mTime;
    }

    public ArcCurveFit(int[] arcModes, double[] time, double[][] y) {
        this.mTime = time;
        this.mArcs = new Arc[time.length - 1];
        int mode = 1;
        int last = 1;
        int i = 0;
        while (i < this.mArcs.length) {
            switch (arcModes[i]) {
                case 0:
                    mode = 3;
                    break;
                case 1:
                    last = 1;
                    mode = 1;
                    break;
                case 2:
                    last = 2;
                    break;
                case 3:
                    mode = last != 1 ? 1 : 2;
                    last = mode;
                    break;
                default:
                    mode = mode;
                    break;
            }
            this.mArcs[i] = new Arc(mode, time[i], time[i + 1], y[i][0], y[i][1], y[i + 1][0], y[i + 1][1]);
            i++;
            mode = mode;
        }
    }

    private static class Arc {
        private static final double EPSILON = 0.001d;
        private static final String TAG = "Arc";
        private static double[] ourPercent = new double[91];
        boolean linear;
        double mArcDistance;
        double mArcVelocity;
        double mEllipseA;
        double mEllipseB;
        double mEllipseCenterX;
        double mEllipseCenterY;
        double[] mLut;
        double mOneOverDeltaTime;
        double mTime1;
        double mTime2;
        double mTmpCosAngle;
        double mTmpSinAngle;
        boolean mVertical;
        double mX1;
        double mX2;
        double mY1;
        double mY2;

        Arc(int mode, double t1, double t2, double x1, double y1, double x2, double y2) {
            this.linear = false;
            this.mVertical = mode == 1;
            this.mTime1 = t1;
            this.mTime2 = t2;
            this.mOneOverDeltaTime = 1.0d / (this.mTime2 - this.mTime1);
            if (3 == mode) {
                this.linear = true;
            }
            double dx = x2 - x1;
            double dy = y2 - y1;
            if (!this.linear && Math.abs(dx) >= EPSILON && Math.abs(dy) >= EPSILON) {
                this.mLut = new double[TypedValues.TYPE_TARGET];
                this.mEllipseA = ((double) (this.mVertical ? -1 : 1)) * dx;
                this.mEllipseB = ((double) (this.mVertical ? 1 : -1)) * dy;
                this.mEllipseCenterX = this.mVertical ? x2 : x1;
                this.mEllipseCenterY = this.mVertical ? y1 : y2;
                buildTable(x1, y1, x2, y2);
                this.mArcVelocity = this.mOneOverDeltaTime * this.mArcDistance;
                return;
            }
            double d = y1;
            double d2 = x2;
            double dy2 = dy;
            double dy3 = x1;
            this.linear = true;
            this.mX1 = dy3;
            this.mX2 = d2;
            this.mY1 = d;
            this.mY2 = y2;
            this.mArcDistance = Math.hypot(dy2, dx);
            this.mArcVelocity = this.mOneOverDeltaTime * this.mArcDistance;
            this.mEllipseCenterX = dx / (this.mTime2 - this.mTime1);
            this.mEllipseCenterY = dy2 / (this.mTime2 - this.mTime1);
        }

        void setPoint(double time) {
            double percent = (this.mVertical ? this.mTime2 - time : time - this.mTime1) * this.mOneOverDeltaTime;
            double angle = lookup(percent) * 1.5707963267948966d;
            this.mTmpSinAngle = Math.sin(angle);
            this.mTmpCosAngle = Math.cos(angle);
        }

        double getX() {
            return this.mEllipseCenterX + (this.mEllipseA * this.mTmpSinAngle);
        }

        double getY() {
            return this.mEllipseCenterY + (this.mEllipseB * this.mTmpCosAngle);
        }

        double getDX() {
            double vx = this.mEllipseA * this.mTmpCosAngle;
            double vy = (-this.mEllipseB) * this.mTmpSinAngle;
            double norm = this.mArcVelocity / Math.hypot(vx, vy);
            return this.mVertical ? (-vx) * norm : vx * norm;
        }

        double getDY() {
            double vx = this.mEllipseA * this.mTmpCosAngle;
            double vy = (-this.mEllipseB) * this.mTmpSinAngle;
            double norm = this.mArcVelocity / Math.hypot(vx, vy);
            return this.mVertical ? (-vy) * norm : vy * norm;
        }

        public double getLinearX(double t) {
            double t2 = (t - this.mTime1) * this.mOneOverDeltaTime;
            double t3 = this.mX1;
            return t3 + ((this.mX2 - this.mX1) * t2);
        }

        public double getLinearY(double t) {
            double t2 = (t - this.mTime1) * this.mOneOverDeltaTime;
            double t3 = this.mY1;
            return t3 + ((this.mY2 - this.mY1) * t2);
        }

        public double getLinearDX(double t) {
            return this.mEllipseCenterX;
        }

        public double getLinearDY(double t) {
            return this.mEllipseCenterY;
        }

        double lookup(double v) {
            if (v <= 0.0d) {
                return 0.0d;
            }
            if (v >= 1.0d) {
                return 1.0d;
            }
            double pos = ((double) (this.mLut.length - 1)) * v;
            int iv = (int) pos;
            double off = pos - ((double) ((int) pos));
            return this.mLut[iv] + ((this.mLut[iv + 1] - this.mLut[iv]) * off);
        }

        private void buildTable(double x1, double y1, double x2, double y2) {
            int i;
            double a;
            double b;
            double a2 = x2 - x1;
            double b2 = y1 - y2;
            double lx = 0.0d;
            double ly = 0.0d;
            double dist = 0.0d;
            int i2 = 0;
            while (i2 < ourPercent.length) {
                double angle = Math.toRadians((((double) i2) * 90.0d) / ((double) (ourPercent.length - 1)));
                double s = Math.sin(angle);
                double c = Math.cos(angle);
                double px = a2 * s;
                double py = b2 * c;
                if (i2 <= 0) {
                    a = a2;
                    b = b2;
                } else {
                    a = a2;
                    double a3 = px - lx;
                    b = b2;
                    double b3 = py - ly;
                    dist += Math.hypot(a3, b3);
                    ourPercent[i2] = dist;
                }
                lx = px;
                ly = py;
                i2++;
                a2 = a;
                b2 = b;
            }
            this.mArcDistance = dist;
            for (int i3 = 0; i3 < ourPercent.length; i3++) {
                double[] dArr = ourPercent;
                dArr[i3] = dArr[i3] / dist;
            }
            int i4 = 0;
            while (i4 < this.mLut.length) {
                double pos = ((double) i4) / ((double) (this.mLut.length - 1));
                int index = Arrays.binarySearch(ourPercent, pos);
                if (index >= 0) {
                    this.mLut[i4] = ((double) index) / ((double) (ourPercent.length - 1));
                    i = i4;
                } else if (index == -1) {
                    this.mLut[i4] = 0.0d;
                    i = i4;
                } else {
                    int p1 = (-index) - 2;
                    int p2 = (-index) - 1;
                    i = i4;
                    double ans = (((double) p1) + ((pos - ourPercent[p1]) / (ourPercent[p2] - ourPercent[p1]))) / ((double) (ourPercent.length - 1));
                    this.mLut[i] = ans;
                }
                i4 = i + 1;
            }
        }
    }
}
