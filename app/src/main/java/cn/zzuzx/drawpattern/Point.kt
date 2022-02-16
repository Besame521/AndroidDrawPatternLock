package cn.zzuzx.drawpattern

/**
 * 节点数据
 * @property x Float 节点X坐标
 * @property y Float 节点Y坐标
 * @property index Int 节点的编号
 * @property isSelected Boolean 节点是否被选中，默认未选中。
 * @constructor
 */
data class Point(val x: Float, val y: Float, val index: Int, var isSelected: Boolean = false)
