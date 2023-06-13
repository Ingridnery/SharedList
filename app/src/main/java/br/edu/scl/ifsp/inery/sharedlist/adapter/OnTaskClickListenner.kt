package br.edu.scl.ifsp.inery.sharedlist.adapter

interface OnTaskClickListenner {
    fun onTileTaskClick(position: Int)

    fun onEditMenuItemClick(position: Int)

    fun onRemoveMenuItemClick(position: Int)
    fun onDetailMenuItemClick(position: Int)
    fun onConcludeMenuItemClick(position: Int)
}