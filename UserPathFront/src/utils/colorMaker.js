/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
export class MyColor {
  constructor() {
    this.TextColor = "FFFFFF";
    this.Color = "FF0000";
  }
}

export class ColorMaker {
  constructor(total) {
    this.TotalColors = total || 300;
  }
  GetColor(seed = 0) {
    const ret = new MyColor();
    // 计算对应下标
    const idx = seed % this.TotalColors;
    // 计算颜色
    const colorVal = this._CalColor(idx);
    // 转成RGB 16进制字符串
    ret.Color = colorVal.toString(16).padStart(6, "0");
    // 计算互补色
    ret.TextColor = this._CalTextColor(ret.Color);

    return ret;
  }

  _CalColor(idx = 0) {
    // 默认返回红色
    let ret = 0xff0000;
    // RGB的最大值
    const full = 0xffffff;
    // 总共需要支持多少种颜色，若传0则取255
    const total = this.TotalColors > 0 ? this.TotalColors : 0xff;
    // 将所有颜色平均分成x份
    const perVal = full / total;
    if (idx >= 0 && idx <= total) {
      ret = perVal * idx;
    }
    ret = Math.round(ret);
    return ret;
  }

  // 计算传入颜色的互补色
  _CalTextColor(input = "") {
    const R = input.substring(0, 2);
    const G = input.substring(2, 2);
    const B = input.substring(4, 2);
    const rVal = parseInt(R, 16);
    const gVal = parseInt(G, 16);
    const bVal = parseInt(B, 16);
    const hsl = this.rgbToHsl(rVal, gVal, bVal);

    hsl.L = (hsl.L + 0.5) % 1.0;
    const rgb = this.hslToRgb(hsl.H, hsl.S, hsl.L);
    const ret = (rgb.R << 16) + (rgb.G << 8) + rgb.B;
    return ret.toString(16).padStart(6, "0");
  }

  rgbToHsl(r, g, b) {
    (r /= 255), (g /= 255), (b /= 255);
    const max = Math.max(r, g, b);
    const min = Math.min(r, g, b);
    let h;
    let s;
    const l = (max + min) / 2;
    if (max == min) {
      h = 0; // achromatic
      s = 0;
    } else {
      const d = max - min;
      s = l > 0.5 ? d / (2 - max - min) : d / (max + min);
      switch (max) {
        case r:
          h = (g - b) / d + (g < b ? 6 : 0);
          break;
        case g:
          h = (b - r) / d + 2;
          break;
        case b:
          h = (r - g) / d + 4;
          break;
      }
      h /= 6;
    }
    return { H: h, S: s, L: l };
  }

  hslToRgb(h, s, l) {
    let r;
    let g;
    let b;
    if (s == 0) {
      r = l; // achromatic、
      g = l;
      b = l;
    } else {
      const hue2rgb = function hue2rgb(p, q, t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1 / 6) return p + (q - p) * 6 * t;
        if (t < 1 / 2) return q;
        if (t < 2 / 3) return p + (q - p) * (2 / 3 - t) * 6;
        return p;
      };
      const q = l < 0.5 ? l * (1 + s) : l + s - l * s;
      const p = 2 * l - q;
      r = hue2rgb(p, q, h + 1 / 3);
      g = hue2rgb(p, q, h);
      b = hue2rgb(p, q, h - 1 / 3);
    }
    return {
      R: Math.round(r * 255),
      G: Math.round(g * 255),
      B: Math.round(b * 255),
    };
  }
}
