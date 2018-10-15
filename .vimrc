set nocompatible
filetype off
" set the runtime path to include Vundle and initialize
set rtp+=~/.vim/bundle/Vundle.vim

call vundle#begin('~/.vim/plugged')

" alternatively, pass a path where Vundle should install plugins
"call vundle#begin('~/some/path/here')

" let Vundle manage Vundle, required
Plugin 'VundleVim/Vundle.vim'
Plugin 'iamcco/markdown-preview.vim'
Plugin 'plugged/vim-pandoc'
Plugin 'tpope/vim-surround'
Plugin 'vim-airline/vim-airline'
Plugin 'vim-airline/vim-airline-themes'
Plugin 'dylanaraps/wal'
"Plugin 'plugged/vim-pandoc-syntax'

call vundle#end()
filetype plugin indent on

" Brief Vundle help
" :PluginList       - lists configured plugins
" :PluginInstall    - installs plugins; append `!` to update or just :PluginUpdate
" :PluginSearch foo - searches for foo; append `!` to refresh local cache
" :PluginClean      - confirms removal of unused plugins; append `!` to auto-approve removal
" see :h vundle for more details or wiki for FAQ

colorscheme desert 
let g:airline_theme='base16_default'
syntax enable
set tabstop=2       "number of visual spaces per tab
set softtabstop=2   "number of spaces in tab when editing
set expandtab       "tabs are spaces
set number          "show line numbers
set showcmd         "show command in bottom bar
set cursorline      "hilight current line
filetype indent on  "load filetype-specific indent files
set wildmenu        "visual autocomplete for command menu
set autoindent
set tw=80           "new line at 80 characters
set formatoptions-=cro  "if line wraps durring bullet, won't create new bullet

func! WordProcessor()
set spell spellang=en_us
setlocal formatoptions=ctnqro
setlocal comments+=n:*,n:#
endfu
com! WP call WordProcessor()

set autoindent
set tw=9999
syntax enable
inoremap ' ''<ESC>ha
inoremap " ""<ESC>ha
inoremap ` ``<ESC>ha
inoremap ( ()<ESC>ha
inoremap [ []<ESC>ha
inoremap { {}<ESC>ha
inoremap /* /** */<ESC>2ha
